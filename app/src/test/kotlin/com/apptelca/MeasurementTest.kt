package com.apptelca

import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.data.mapper.toDomain
import com.apptelca.data.mapper.toDomainList
import com.apptelca.data.mapper.toEntity
import com.apptelca.data.mapper.toEntityList
import com.apptelca.domain.model.Measurement
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Realiza pruebas unitarias locales de funciones para medidas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas unitarias locales de funciones para medidas")
class MeasurementTest {
    /**
     * Realiza pruebas unitarias locales de las funciones del archivo
     * MeasurementMappers.kt.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @DisplayName("MeasurementMappers")
    @Nested
    inner class MeasurementMappersTests {
        @DisplayName("Convierte una MeasurementEntity en un modelo de dominio Measurement")
        @Test
        fun measurementEntity_toDomain_isCorrect() {
            val measurementEntity = MeasurementEntity(
                id = 1,
                workId = 1,
                measurementType = MeasurementType.ANCHO_TELA,
                value = 150.0
            )

            assertEquals(
                measurementEntity.id,
                measurementEntity.toDomain().id
            )
        }

        @DisplayName("Convierte una lista de MeasurementEntity en una lista de modelos de dominio Measurement")
        @Test
        fun listMeasurementEntity_toDomainList_isCorrect() {
            val measurementsList = listOf(
                MeasurementEntity(
                    id = 1,
                    workId = 1,
                    measurementType = MeasurementType.ANCHO_TELA,
                    value = 150.0
                )
            )

            assertEquals(
                measurementsList[0].id,
                measurementsList.toDomainList()[0].id
            )
        }

        @DisplayName("Convierte un modelo de dominio Measurement en una MeasurementEntity")
        @Test
        fun measurement_toEntity_isCorrect() {
            val measurement = Measurement(
                id = 1,
                workId = 1,
                measurementType = MeasurementType.ANCHO_TELA,
                value = 150.0
            )

            assertEquals(measurement.id, measurement.toEntity().id)
        }

        @DisplayName("Convierte una lista de modelos de dominio Measurement en una lista de MeasurementEntity")
        @Test
        fun listMeasurement_toEntityList_isCorrect() {
            val measurementsList = listOf(
                Measurement(
                    id = 1,
                    workId = 1,
                    measurementType = MeasurementType.ANCHO_TELA,
                    value = 150.0
                )
            )

            assertEquals(
                measurementsList[0].id,
                measurementsList.toEntityList()[0].id
            )
        }
    }
}