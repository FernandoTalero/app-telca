package com.apptelca.validator

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import com.apptelca.core.Util

/**
 * Valida números Double mayores que cero y obtenidos a
 * partir de una cadena de texto.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class GreaterThanZeroValidator(private val resourceProvider: ResourceProvider) : Validator {
    /**
     * Valida que el número (Double) obtenido a partir de una cadena de texto
     * sea mayor que cero.
     *
     * @param text Texto a partir del cual se obtiene el número a validar.
     * @return null si no hay error o una cadena de texto del mismo en caso contrario.
     */
    override fun validate(text: String): String? {
        val number = Util.stringToDouble(text)
        // Si el valor numérico es menor o igual que cero, retornamos el mensaje de error
        if(number <= 0.0) {
            return resourceProvider.getString(R.string.no_valid_number_error)
        }

        return null
    }
}