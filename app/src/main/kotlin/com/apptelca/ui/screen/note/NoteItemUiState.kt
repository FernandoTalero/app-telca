package com.apptelca.ui.screen.note

/**
 * Almacena el estado de la UI para cada item de la lista de notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class NoteItemUiState(
    val id: Long? = null,
    val noteDateTime: String = "",
    val noteText: String = "",
    val isSelected: Boolean = false
)