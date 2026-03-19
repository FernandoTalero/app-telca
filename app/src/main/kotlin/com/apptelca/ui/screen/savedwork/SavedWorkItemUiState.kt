package com.apptelca.ui.screen.savedwork

/**
 * Almacena el estado de la UI para cada item de la lista de trabajos guardados.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class SavedWorkItemUiState(
    val workId: Long = 0L,
    val workName: String = "",
    val workType: String = "",
    val workSubType: String = "",
    val workDateTime: String = "",
    val isSelected: Boolean = false
)