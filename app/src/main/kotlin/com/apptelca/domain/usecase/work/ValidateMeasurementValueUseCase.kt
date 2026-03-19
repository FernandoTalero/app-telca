package com.apptelca.domain.usecase.work

import com.apptelca.validator.MeasurementValueValidator
import javax.inject.Inject

/**
 * Valida el valor de una medida.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ValidateMeasurementValueUseCase @Inject constructor(
    private val measurementValueValidator: MeasurementValueValidator
) {
    /**
     * Valida el valor de una medida.
     *
     * @param value El valor de la medida a validar.
     * @return True si el valor es válido; false en caso contrario.
     */
    operator fun invoke(value: String): Boolean {
        return measurementValueValidator.validate(value) == null
    }
}