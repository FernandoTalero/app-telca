package com.apptelca.domain.model

import com.apptelca.domain.model.garment.subgarment.MeasurementType

/**
 * Clase modelo de dominio para una medida.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class Measurement(
    val id: Long = 0,
    val workId: Long,
    val measurementType: MeasurementType,
    val value: Double
)