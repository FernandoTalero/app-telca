package com.apptelca.validator

/**
 * Valida el nombre de un trabajo.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class WorkNameValidator : Validator {
    /**
     * Valida que el nombre de un trabajo tenga como máximo 50 caracteres y
     * no esté vacío.
     *
     * @param text Nombre del trabajo.
     * @return null si no hay error o una cadena de texto vacía en caso contrario.
     */
    override fun validate(text: String): String? {
        // Establecemos un límite de 50 caracteres y que el nombre no esté vacío
        val t = text.trim()
        if(t.length < 51 && t.isNotEmpty()) {
            return null
        }

        return "" // En este caso, no necesitamos un mensaje de error
    }
}