package com.apptelca.ui.screen.savedwork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apptelca.core.ResourceProvider
import com.apptelca.core.Util
import com.apptelca.domain.model.Work
import com.apptelca.domain.usecase.SavedWorkUseCase
import com.apptelca.ui.event.UiEvent
import com.apptelca.ui.screen.work.WorkSession
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

/**
 * Conecta la capa de dominio con la UI de la screen SavedWork.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@HiltViewModel
class SavedWorkViewModel @Inject constructor(
    val resourceProvider: ResourceProvider,
    private val savedWorkUseCase: SavedWorkUseCase
) : ViewModel() {
    //StateFlow con la lista de trabajos guardados
    private val savedWorks: StateFlow<List<Work>> = savedWorkUseCase.getWorksUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // StateFlow con un Set de ids de trabajos seleccionados
    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds: StateFlow<Set<Long>> = _selectedIds.asStateFlow()

    // StateFlow para conservar el estado de la UI
    private val _uiState = MutableStateFlow(SavedWorkUiState())
    val uiState: StateFlow<SavedWorkUiState> = _uiState.asStateFlow()

    // Inicializamos el StateFlow
    init {
        viewModelScope.launch {
            combine(savedWorks, selectedIds) { works, selectedIds ->
                // Obtenemos una lista de objetos SavedWorkItemState
                val savedWorksItemsList = works.map {
                    SavedWorkItemUiState(
                        workId = it.id,
                        workName = it.name,
                        workType = resourceProvider.getGarmentTypeString(it.type),
                        workSubType = resourceProvider.getGarmentSubTypeString(it.subtype),
                        workDateTime = Util.formatDateTime(it.dateTime),
                        isSelected = selectedIds.contains(it.id)
                    )
                }

                // Convertimos la lista en un Map<Long, SavedWorkItemState>
                val savedWorksItems = savedWorksItemsList.associateBy { it.workId }

                // Retornamos el estado actualizado de la UI
                _uiState.value.copy(
                    savedWorksItems = savedWorksItems,
                    selectedItemsCount = selectedIds.size
                )
            }.collect { savedWorkUiState -> _uiState.value = savedWorkUiState }
        }
    }

    // SharedFlow para notificar los eventos de la UI
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    /**
     * Inicia el modo de selección.
     */
    fun setInSelectionMode() {
        _uiState.update { it.copy(isInSelectionMode = true) }
    }

    /**
     * Alterna la selección de un item de la lista de trabajos.
     *
     * @param workId El ID del trabajo cuyo item se selecciona o deselecciona.
     */
    fun toggleSelection(workId: Long) {
        _selectedIds.update {
            // Si el id del trabajo se encuentra en el Set de ids seleccionados, lo quitamos
            if(it.contains(workId)) {
                it - workId
            } else {
                // En caso contrario, lo añadimos
                it + workId
            }
        }

        /* Si, al pulsar sobre un item, no queda ninguno seleccionado, se cierra
        * la TopAppBar contextual. */
        if(selectedIds.value.isEmpty()) {
            closeContextualTopAppBar()
        }
    }

    /**
     * Muestra el diálogo de confirmación para eliminar trabajos guardados.
     */
    fun showDeleteWorksDialog() {
        _uiState.update { it.copy(showDeleteWorksDialog = true) }
    }

    /**
     * Oculta el diálogo de confirmación para eliminar trabajos guardados.
     */
    fun dismissDeleteWorksDialog() {
        _uiState.update { it.copy(showDeleteWorksDialog = false) }
    }

    /**
     * Elimina los trabajos seleccionados
     */
    fun deleteSelectedWorks() {
        // Obtenemos los ids de los trabajos seleccionados
        val selectedIdsSet = selectedIds.value

        /* Si la lista de ids seleccionados está vacía, no hacemos nada para
        * evitar que se lance la coroutine inútilmente. */
        if(selectedIdsSet.isEmpty()) return

        viewModelScope.launch {
            try {
                /* Filtramos la lista de trabajos guardados (objetos Work) para obtener
                * sólo los seleccionados*/
                val worksToDelete = savedWorks.value.filter { selectedIdsSet.contains(it.id) }
                if(worksToDelete.isNotEmpty()) {
                    // Eliminamos los trabajos seleccionados
                    val deletedWorks = savedWorkUseCase.deleteWorksUseCase(worksToDelete)

                    // Indicamos si el trabajo cuyo id se encuentra en la sesión ha sido eliminado
                    if(selectedIdsSet.contains(WorkSession.activeWorkId)) {
                        WorkSession.activeWorkId = 0
                    }

                    // Emitimos el evento de borrado a la UI
                    _uiEvent.emit(UiEvent.WorkDeleted(deletedWorks))

                    // Cerramos la barra contextual
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
        // Limpiamos la selección de trabajos
        clearSelection()
    }

    /**
     * Limpia la selección de trabajos.
     */
    private fun clearSelection() {
        _selectedIds.value = emptySet()
    }
}