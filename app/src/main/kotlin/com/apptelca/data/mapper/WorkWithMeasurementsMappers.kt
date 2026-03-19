package com.apptelca.data.mapper

import com.apptelca.data.local.entities.WorkWithMeasurementsEntity
import com.apptelca.domain.model.WorkWithMeasurements

/**
 * Convierte una WorkWithMeasurementsEntity en un modelo de dominio WorkWithMeasurements.
 *
 * @return Un modelo de dominio WorkWithMeasurement.
 */
fun WorkWithMeasurementsEntity.toDomain(): WorkWithMeasurements {
    return WorkWithMeasurements(
        work = workEntity.toDomain(),
        measurements = measurementEntities.toDomainList()
    )
}