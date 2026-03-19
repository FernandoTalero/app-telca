package com.apptelca.domain.model

/**
 * Clase modelo de dominio para la relación entre un trabajo y sus medidas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class WorkWithMeasurements(
    val work: Work,
    val measurements: List<Measurement>
)