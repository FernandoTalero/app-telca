package com.apptelca.data.mapper

import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.domain.model.Measurement

/**
 * Convierte una MeasurementEntity en un modelo de dominio Measurement.
 *
 * @return Un modelo de dominio Measurement.
 */
fun MeasurementEntity.toDomain(): Measurement {
    return Measurement(
        id = id,
        workId = workId,
        measurementType = measurementType,
        value = value
    )
}

/**
 * Convierte una lista de MeasurementEntity en una lista de modelos de dominio Measurement.
 *
 * @return Una lista de modelos de dominio Measurement.
 */
fun List<MeasurementEntity>.toDomainList(): List<Measurement> {
    return this.map { it.toDomain() }
}

/**
 * Convierte un modelo de dominio Measurement en una MeasurementEntity.
 *
 * @return Una MeasurementEntity.
 */
fun Measurement.toEntity(): MeasurementEntity {
    return MeasurementEntity(
        id = id,
        workId = workId,
        measurementType = measurementType,
        value = value
    )
}

/**
 * Convierte una lista de modelos de dominio Measurement en una lista de MeasurementEntity.
 *
 * @return Una lista de MeasurementEntity.
 */
fun List<Measurement>.toEntityList(): List<MeasurementEntity> {
    return this.map { it.toEntity() }
}