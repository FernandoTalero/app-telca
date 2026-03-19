package com.apptelca.ui.component

/**
 * Almacena el estado de un item de TDropdownMenu.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class DropdownMenuItemUiState<T : Any>(
    val text: String,
    val value: T
) {
    /**
     * Retorna la propiedad "text" para que el texto del item
     * se vea correctamente en el menú desplegable.
     */
    override fun toString(): String {
        return text
    }
}