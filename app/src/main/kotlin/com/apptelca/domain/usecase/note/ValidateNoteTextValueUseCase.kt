package com.apptelca.domain.usecase.note

import com.apptelca.validator.NoteTextValueValidator

/**
 * Valida el valor del campo de texto para el text de una nota.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ValidateNoteTextValueUseCase(private val noteTextValueValidator: NoteTextValueValidator) {
    /**
     * Valida el valor del campo de texto para el text de una nota.
     *
     * @param text El texto de la nota a validar.
     * @return True si el nombre es válido; false en caso contrario.
     */
    operator fun invoke(text: String): Boolean {
        return noteTextValueValidator.validate(text) == null
    }
}