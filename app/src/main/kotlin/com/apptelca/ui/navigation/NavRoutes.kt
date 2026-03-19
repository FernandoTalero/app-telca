package com.apptelca.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Contiene las rutas para la navegación entre screens.
 */
@Serializable
sealed class NavRoutes {
    /**
     * Ruta para la screen principal (trabajo).
     */
    @Serializable
    data class Work(val workId: Long = 0L) : NavRoutes()

    /**
     * Ruta para la screen de notas.
     */
    @Serializable
    data class Note(val workId: Long = 0L) : NavRoutes()

    /**
     * Ruta para la screen de trabajos guardados.
     */
    @Serializable
    object SavedWork : NavRoutes()

    /**
     * Ruta para la screen "Acerca de".
     */
    @Serializable
    object About : NavRoutes()
}