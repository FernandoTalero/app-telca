package com.apptelca.ui.screen.savedwork

/**
 * Almacena el estado de la UI de la screen SavedWork.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class SavedWorkUiState(
    // Pantalla principal
    val savedWorksItems: Map<Long, SavedWorkItemUiState> = emptyMap(),
    val isInSelectionMode: Boolean = false,
    val selectedItemsCount: Int = 0,

    // Diálogo para la confirmación de borrado de trabajos guardados
    val showDeleteWorksDialog: Boolean = false
)