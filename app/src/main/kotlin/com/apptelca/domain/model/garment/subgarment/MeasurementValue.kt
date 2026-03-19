package com.apptelca.domain.model.garment.subgarment

/**
 * Clase que modela un par MeasurementType-Double para las
 * medidas de las prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class MeasurementValue(
    val measurementType: MeasurementType,
    val value: Double
)