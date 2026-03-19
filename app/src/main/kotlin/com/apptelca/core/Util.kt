package com.apptelca.core

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Contiene funciones para utilidades varias.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
object Util {
    /**
     * Convierte una cadena de texto en un Double.
     *
     * @param text La cadena de texto a convertir.
     * @return El valor Double resultado de la conversión o 0.0 en caso de
     * que ésta no sea posible.
     */
    fun stringToDouble(text: String): Double {
        return text.trim().replace(",", ".").toDoubleOrNull() ?: 0.0
    }

    /**
     * Da formato de fecha y hora a un objeto de tipo Instant.
     *
     * @param dateTime El objeto Instant al que se quiere dar formato.
     * @return Una cadena de texto con la fecha formateada.
     */
    fun formatDateTime(dateTime: Instant): String {
        return DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault())
            .format(dateTime)
    }

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