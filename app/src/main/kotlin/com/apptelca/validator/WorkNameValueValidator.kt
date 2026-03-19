package com.apptelca.validator

/**
 * Valida el valor del campo de texto para el nombre de un trabajo antes de ser
 * almacenado.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class WorkNameValueValidator : Validator {
    /**
     * Valida que el valor del nombre de un trabajo tenga como máximo 50 caracteres.
     *
     * @param text Texto del valor del campo de texto del nombre del trabajo.
     * @return null si no hay error o una cadena de texto vacía en caso contrario.
     */
    override fun validate(text: String): String? {
        // Establecemos un límite de 50 caracteres
        if(text.length < 51) {
            return null
        }

        return "" // En este caso, no necesitamos un mensaje de error
    }
}