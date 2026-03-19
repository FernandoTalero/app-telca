package com.apptelca.validator

/**
 * Define la base para clases validadoras.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface Validator {
    /**
     * Valida una cadena de texto.
     *
     * @param text La cadena de texto a validar.
     * @return null si la validación es exitosa o un mensaje de error en caso contrario.
     */
    fun validate(text: String): String?
}