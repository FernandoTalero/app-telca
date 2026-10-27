package com.apptelca.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Interfaz con las rutas para la navegación entre screens.
 *
 * @author Fernando Talero
 * @version 1.1
 * @since 1.0
 */
@Serializable
sealed interface NavRoutes {
    /**
     * Ruta para la screen principal (trabajo).
     */
    @Serializable
    data class Work(val workId: Long = 0L) : NavRoutes

    /**
     * Ruta para la screen de notas.
     */
    @Serializable
    data class Note(val workId: Long = 0L) : NavRoutes

    /**
     * Ruta para la screen de trabajos guardados.
     */
    @Serializable
    data object SavedWork : NavRoutes

    /**
     * Ruta para la screen "Acerca de".
     */
    @Serializable
    data object About : NavRoutes
}