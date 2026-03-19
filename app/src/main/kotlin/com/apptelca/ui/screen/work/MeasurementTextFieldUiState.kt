package com.apptelca.ui.screen.work

import com.apptelca.domain.model.garment.subgarment.MeasurementType

/**
 * Almacena el estado de un TextField para una medida de un subtipo
 * de prenda de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class MeasurementTextFieldUiState(
    val measurementType: MeasurementType? = null,
    val value: String = "",
    val label: String = "",
    val placeholder: String = "",
    val isError: Boolean = false,
    val error: String? = null
)