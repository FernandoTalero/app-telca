package com.apptelca.ui.component

/**
 * Almacena el estado del TextField para el nombre de un trabajo.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class SimpleTextFieldUiState(
    val value: String = "",
    val label: String = "",
    val placeholder: String = "",
    val isError: Boolean = false,
    val error: String? = null
)