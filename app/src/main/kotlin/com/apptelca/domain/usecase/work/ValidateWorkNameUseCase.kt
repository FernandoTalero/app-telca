package com.apptelca.domain.usecase.work

import com.apptelca.validator.WorkNameValidator

/**
 * Valida el nombre de un trabajo.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ValidateWorkNameUseCase(private val workNameValidator: WorkNameValidator) {
    /**
     * Valida el nombre de un trabajo.
     *
     * @param name El nombre del trabajo a validar.
     * @return True si el nombre es válido; false en caso contrario.
     */
    operator fun invoke(name: String): Boolean {
        return workNameValidator.validate(name) == null
    }
}