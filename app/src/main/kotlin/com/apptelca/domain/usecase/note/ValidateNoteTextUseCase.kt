package com.apptelca.domain.usecase.note

import com.apptelca.validator.NoteTextValidator

/**
 * Valida el texto de una nota.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ValidateNoteTextUseCase(private val noteTextValidator: NoteTextValidator) {
    /**
     * Valida el texto de una nota.
     *
     * @param text El texto de la nota a validar.
     * @return True si el nombre es válido; false en caso contrario.
     */
    operator fun invoke(text: String): Boolean {
        return noteTextValidator.validate(text) == null
    }
}