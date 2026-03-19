package com.apptelca.ui.screen.note

import com.apptelca.ui.component.SimpleTextFieldUiState

/**
 * Almacena el estado de la UI de la screen Note.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class NoteUiState(
    // Pantalla principal
    val notesItems: Map<Long, NoteItemUiState> = emptyMap(),
    val currentNoteItem: NoteItemUiState = NoteItemUiState(),
    val isInSelectionMode: Boolean = false,
    val selectedItemsCount: Int = 0,

    // Diálogo para la confirmación de borrado de notas
    val showDeleteNotesDialog: Boolean = false,

    // BottomSheet para guardar o actualizar una nota
    val showBottomSheet: Boolean = false,
    val bottomSheetNoteTextField: SimpleTextFieldUiState = SimpleTextFieldUiState()
)