package com.apptelca.validator

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import javax.inject.Inject

/**
 * Valida una medida.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class MeasurementValidator @Inject constructor(
    private val resourceProvider: ResourceProvider
) : Validator {
    /**
     * Valida que una medida tenga menos de siete caracteres y sea un número decimal.
     *
     * @param text Texto a partir del cual se obtiene la medida a validar.
     * @return null si no hay error o una cadena de texto del mismo en caso contrario.
     */
    override fun validate(text: String): String? {
        // Establecemos un límite de seis caracteres
        if(text.length < 7) {
            // Filtramos la entrada de datos para asegurar que sean números decimales
            if(text.isNotEmpty() && text.matches(Regex("^\\d+\\.?\\d*$"))) {
                return null
            }
        }

        return resourceProvider.getString(R.string.no_valid_measurement_value)
    }
}