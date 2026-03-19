package com.apptelca.domain.usecase.work

import com.apptelca.validator.MeasurementValidator

/**
 * Valida una medida.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ValidateMeasurementUseCase(
    private val measurementValidator: MeasurementValidator
) {
    /**
     * Valida una medida.
     *
     * @param value La medida a validar.
     * @return True si el valor es válido; false en caso contrario.
     */
    operator fun invoke(value: String): Boolean {
        return measurementValidator.validate(value) == null
    }
}