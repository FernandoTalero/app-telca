package com.apptelca.validator

/**
 * Valida el texto de una nota.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class NoteTextValidator : Validator {
    /**
     * Valida que el texto de una nota tenga como máximo 200 caracteres y
     * no esté vacío.
     *
     * @param text Texto de la nota.
     * @return null si no hay error o una cadena de texto vacía en caso contrario.
     */
    override fun validate(text: String): String? {
        // Establecemos un límite de 200 caracteres y que el texto no esté vacío
        val t = text.trim()
        if(t.length < 201 && t.isNotEmpty()) {
            return null
        }

        return "" // En este caso, no necesitamos un mensaje de error
    }
}