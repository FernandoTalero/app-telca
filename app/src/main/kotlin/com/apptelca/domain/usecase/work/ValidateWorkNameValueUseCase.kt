package com.apptelca.domain.usecase.work

import com.apptelca.validator.WorkNameValueValidator

/**
 * Valida el valor del campo de texto para el nombre de un trabajo.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ValidateWorkNameValueUseCase(
    private val workNameValueValidator: WorkNameValueValidator
) {
    /**
     * Valida el valor del campo de texto para el nombre de un trabajo.
     *
     * @param value El valor del nombre del trabajo a validar.
     * @return True si el valor es válido; false en caso contrario.
     */
    operator fun invoke(value: String): Boolean {
        return workNameValueValidator.validate(value) == null
    }
}