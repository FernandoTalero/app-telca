package com.apptelca.validator

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import javax.inject.Inject

/**
 * Valida el valor de una medida.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class MeasurementValueValidator @Inject constructor(
    private val resourceProvider: ResourceProvider
) : Validator {
    /**
     * Valida que el valor de una medida tenga menos de siete caracteres.
     *
     * @param text Texto a partir del cual se obtiene el valor de la medida a validar.
     * @return null si no hay error o una cadena de texto del mismo en caso contrario.
     */
    override fun validate(text: String): String? {
        // Establecemos un límite de seis caracteres
        if(text.length < 7) {
            return null
        }

        return resourceProvider.getString(R.string.no_valid_measurement_value)
    }
}