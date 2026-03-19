package com.apptelca.ui.screen.note

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apptelca.R
import com.apptelca.core.ResourceProvider
import com.apptelca.core.Util
import com.apptelca.di.IoDispatcher
import com.apptelca.domain.model.Note
import com.apptelca.domain.usecase.NoteUseCase
import com.apptelca.ui.event.UiEvent
import com.apptelca.ui.navigation.NavRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

/**
 * Conecta la capa de dominio con la UI de la screen NoteWork.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@HiltViewModel
class NoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val resourceProvider: ResourceProvider,
    private val noteUseCase: NoteUseCase,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    /* Inicializamos el id del trabajo al que pertenecen las notas a partir de la
    * ruta de navegación. */
    private val workId = savedStateHandle.toRoute<NavRoutes.Note>().workId

    // StateFlow con la lista de notas
    private val notes: StateFlow<List<Note>> = noteUseCase.getNotesUseCase(workId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // StateFlow con un Set de ids de notas seleccionadas
    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds: StateFlow<Set<Long>> = _selectedIds.asStateFlow()

    // StateFlow para conservar el estado de la UI
    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    // Inicializamos el StateFlow
    init {
        viewModelScope.launch {
            combine(notes, selectedIds) { notes, selectedIds ->
                // Obtenemos una lista de objetos NoteItemState
                val notesItemsList = notes.map {
                    NoteItemUiState(
                        id = it.id,
                        noteDateTime = Util.formatDateTime(it.dateTime),
                        noteText = it.text,
                        isSelected = selectedIds.contains(it.id)
                    )
                }

                // Convertimos la lista en un Map<Long, NoteItemState>
                val notesItems = notesItemsList.associateBy { it.id!! }

                // Retornamos el estado actualizado de la UI
                _uiState.value.copy(
                    notesItems = notesItems,
                    selectedItemsCount = selectedIds.size
                )
            }.collect { noteUiState -> _uiState.value = noteUiState }
        }
    }

    // ShardFlow para notificar los eventos de la UI
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    /**
     * Inicia el modo de selección.
     */
    fun setInSelectionMode() {
        _uiState.update { it.copy(isInSelectionMode = true) }
    }

    /**
     * Alterna la selección de un item de la lista de notas.
     *
     * @param id El id de la nota cuyo item se selecciona o deselecciona.
     */
    fun toggleSelection(id: Long) {
        _selectedIds.update {
            // Si el id de la nota se encuentra en el Set de ids seleccionados, lo quitamos
            if(it.contains(id)) {
                it - id
            } else {
                // En caso contrario, lo añadimos
                it + id
            }
        }

        /* Si, al pulsar sobre un item, no queda ninguno seleccionado, se cierra
        * la TopAppBar contextual. */
        if(selectedIds.value.isEmpty()) {
            closeContextualTopAppBar()
        }
    }

    /**
     * Muestra el BottomSheet para añadir o actualizar una nota.
     *
     * @param noteId El id de la nota a actualizar. Null si es una nueva.
     * @param noteText El texto de la nota a actualizar. Null si es una nueva.
     */
    fun showBottomSheet(noteId: Long? = null, noteText: String = "") {
        _uiState.update {
            val actualNoteItem = it.currentNoteItem.copy(id = noteId, noteText = noteText)
            val noteTextField = it.bottomSheetNoteTextField.copy(value = noteText)

            it.copy(
                currentNoteItem = actualNoteItem,
                showBottomSheet = true,
                bottomSheetNoteTextField = noteTextField
            )
        }
    }

    /**
     * Cierra el BottomSheet para añadir o actualizar una nota.
     */
    fun dismissBottomSheet() {
        _uiState.update {
            val noteTextField = it.bottomSheetNoteTextField.copy(
                value = "",
                isError = false,
                error = null
            )
            it.copy(showBottomSheet = false, bottomSheetNoteTextField = noteTextField)
        }
    }

    /**
     * Actualiza el valor del campo de texto para el texto de una nota.
     *
     * @param value El valor del campo de texto del texto de la nota.
     */
    fun updateBottomSheetTextField(value: String) {
        if(noteUseCase.validateNoteTextValueUseCase(value)) {
            _uiState.update {
                val actualNoteItem = it.currentNoteItem.copy(noteText = value)
                val noteTextField = it.bottomSheetNoteTextField.copy(value = value)

                it.copy(currentNoteItem = actualNoteItem, bottomSheetNoteTextField = noteTextField)
            }
        }
    }

    /**
     * Guarda o actualiza un nota.
     */
    fun bottomSheetSaveButtonClick(onSuccess: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            with(uiState.value) {
                val noteId = currentNoteItem.id
                val noteText = currentNoteItem.noteText
                val noteTextIsError: Boolean
                val noteTextError: String?

                // Si el texto de la nota es válido...
                if(noteUseCase.validateNoteTextUseCase(noteText)) {
                    // Inicializamos las variables de error para que no se muestre ninguno
                    noteTextIsError = false
                    noteTextError = null

                    // Guardamos o actualizamos la nota según si hay un id asociado
                    if(noteId == null) {
                        saveNote(
                            Note(
                                workId = workId,
                                text = noteText,
                                dateTime = Instant.now()
                            )
                        )
                    } else {
                        getNote(noteId)?.copy(text = noteText)?.let {
                            updateNote(it)
                        }
                    }

                    onSuccess()
                } else {
                    /* ... En caso contrario, inicializamos las variables de error para mostrar
                    * el error correspondiente. */
                    noteTextIsError = true
                    noteTextError = resourceProvider.getString(R.string.no_valid_note_text_error)
                }

                // Indicamos al campo de texto del texto de la nota si debe mostrar un error o no
                val updatedTextField = bottomSheetNoteTextField.copy(
                    isError = noteTextIsError,
                    error = noteTextError
                )
                _uiState.update { it.copy(bottomSheetNoteTextField = updatedTextField) }
            }
        }
    }


    /**
     * Muestra el diálogo de confirmación para eliminar notas.
     */
    fun showDeleteNotesDialog() {
        _uiState.update { it.copy(showDeleteNotesDialog = true) }
    }

    /**
     * Oculta el diálogo de confirmación para eliminar trabajos guardados.
     */
    fun dismissDeleteNotesDialog() {
        _uiState.update { it.copy(showDeleteNotesDialog = false) }
    }

    /**
     * Elimina las notas seleccionadas
     */
    fun deleteSelectedNotes() {
        // Obtenemos los ids de los trabajos seleccionados
        val selectedIdsSet = selectedIds.value

        /* Si la lista de ids seleccionados está vacía, no hacemos nada para
        * evitar que se lance la coroutine inútilmente. */
        if(selectedIdsSet.isEmpty()) return

        viewModelScope.launch {
            try {
                // Filtramos la lista de notas para obtener sólo los seleccionados
                val notesToDelete = notes.value.filter { selectedIdsSet.contains(it.id) }
                if(notesToDelete.isNotEmpty()) {
                    // Eliminamos las notas seleccionadas
                    val deletedNotes = noteUseCase.deleteNotesUseCase(notesToDelete)

                    // Emitimos el evento de borrado a la UI
                    _uiEvent.emit(UiEvent.NoteDeleted(deletedNotes))

                    // Cerramos la TopAppBar contextual
                    closeContextualTopAppBar()
                }

            } catch(ex: Exception) {
                _uiEvent.emit(UiEvent.Error("Error: ${ex.message}"))
            }
        }
    }

    /**
     * Cierra la barra contextual.
     */
    fun closeContextualTopAppBar() {
        _uiState.update { it.copy(isInSelectionMode = false) }
        // Limpiamos la selección de notas
        _selectedIds.value = emptySet()
    }

    /**
     * Obtiene un objeto Note.
     *
     * @param noteId El id del objeto Note que se quiere obtener.
     * @return El objeto Note cuyo id se pasa como parámetro o null
     * en caso de que el id no exista.
     */
    private suspend fun getNote(noteId: Long): Note? = withContext(ioDispatcher) {
        try {
            noteUseCase.getNoteUseCase(noteId)
        } catch(_: Exception) {
            null
        }
    }

    /**
     * Guarda una nota en la base de datos.
     *
     * @param note El objeto Note a guardar.
     */
    private fun saveNote(note: Note) {
        viewModelScope.launch {
            try {
                noteUseCase.saveNoteUseCase(note)
                _uiState.update { it.copy(currentNoteItem = NoteItemUiState()) }

                // Emitimos el evento de nota salvada
                _uiEvent.emit(UiEvent.NoteSaved)
            } catch(ex: Exception) {
                _uiEvent.emit(UiEvent.Error("Error: ${ex.message}"))
            }
        }
    }

    /**
     * Actualiza una nota en la base de datos.
     *
     * @param note El objeto Note a actualizar.
     */
    private fun updateNote(note: Note) {
        viewModelScope.launch(ioDispatcher) {
            try {
                noteUseCase.updateNoteUseCase(note)
                _uiState.update { it.copy(currentNoteItem = NoteItemUiState()) }

                // Emitimos el evento de nota actualizada
                _uiEvent.emit(UiEvent.NoteUpdated)
            } catch(ex: Exception) {
                _uiEvent.emit(UiEvent.Error("Error: ${ex.message}"))
            }
        }
    }
}