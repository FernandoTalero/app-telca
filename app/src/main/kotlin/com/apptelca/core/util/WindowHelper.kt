package com.apptelca.core.util

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Contiene funciones para cálculos de la pantalla.
 *
 * @author Fernando Talero
 * @version 1.1
 * @since 1.0
 */
object WindowHelper {
    /**
     * Obtiene el WindowWidthSizeClass de la ventana actual.
     *
     * @param activity La Activity asociada a la ventana actual.
     * @return Un objeto WindowWidthSizeClass.
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun getWindowWidthSizeClass(activity: Activity?): WindowWidthSizeClass {
        return activity?.let {
            val windowWidthSizeClass = calculateWindowSizeClass(it).widthSizeClass
            /* Si el dispositivo no es una tablet y WindowWidthSizeClass es Medium o Expanded
            * (lo que ocurre con el móvil en modo apaisado), retornamos Compact; en caso contrario,
            * el valor correspondiente. */
            if(!isTablet() && (windowWidthSizeClass == WindowWidthSizeClass.Medium ||
                        windowWidthSizeClass == WindowWidthSizeClass.Expanded)
            ) {
                WindowWidthSizeClass.Compact
            } else {
                windowWidthSizeClass
            }
        } ?: if(isTablet()) WindowWidthSizeClass.Expanded else WindowWidthSizeClass.Compact
    }

    /**
     * Comprueba si el dispositivo es una tablet.
     *
     * @return true si es una tablet; false en caso contrario.
     */
    @Composable
    private fun isTablet(): Boolean {
        /* Se considera que un dispositivo es una tablet si su ancho de pantalla mínimo es
        * mayor o igual a 600 dp. */
        return LocalConfiguration.current.smallestScreenWidthDp >= 600
    }
}