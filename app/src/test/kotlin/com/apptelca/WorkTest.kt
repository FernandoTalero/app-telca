package com.apptelca

import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.data.local.entities.WorkWithMeasurementsEntity
import com.apptelca.data.mapper.toDomain
import com.apptelca.data.mapper.toDomainList
import com.apptelca.data.mapper.toEntity
import com.apptelca.data.mapper.toEntityList
import com.apptelca.domain.model.Work
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 * Realiza pruebas unitarias locales de funciones para trabajos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas unitarias locales de funciones para trabajos")
class WorkTest {
    /**
     * Realiza pruebas unitarias locales de las funciones del archivo
     * WorkMappers.kt.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @DisplayName("WorkMappers")
    @Nested
    inner class WorkMappersTests {
        @DisplayName("Convierte una WorkEntity en un modelo de dominio Work")
        @Test
        fun workEntity_toDomain_isCorrect() {
            val workEntity = WorkEntity(
                id = 1,
                type = GarmentType.CAMISA,
                subtype = GarmentSubType.CAMISA_BASICA,
                name = "Test work",
                dateTime = Instant.now()
            )

            assertEquals(workEntity.id, workEntity.toDomain().id)
        }

        @DisplayName("Convierte una lista de WorkEntity en una lista de modelos de dominio Work")
        @Test
        fun workEntitiesList_toDomainList_isCorrect() {
            val workEntitiesList = listOf(
                WorkEntity(
                    id = 1,
                    type = GarmentType.CAMISA,
                    subtype = GarmentSubType.CAMISA_BASICA,
                    name = "Test work",
                    dateTime = Instant.now()
                )
            )

            assertEquals(
                workEntitiesList[0].id,
                workEntitiesList.toDomainList()[0].id
            )
        }

        @DisplayName("Convierte un modelo de dominio Work en una WorkEntity")
        @Test
        fun work_toEntity_isCorrect() {
            val work = Work(
                id = 1,
                type = GarmentType.CAMISA,
                subtype = GarmentSubType.CAMISA_BASICA,
                name = "Test work",
                dateTime = Instant.now()
            )

            assertEquals(work.id, work.toEntity().id)
        }

        @DisplayName("Convierte una lista de modelos de dominio Work en una lista de WorkEntity")
        @Test
        fun worksList_toEntityList_isCorrect() {
            val worksList = listOf(
                Work(
                    id = 1,
                    type = GarmentType.CAMISA,
                    subtype = GarmentSubType.CAMISA_BASICA,
                    name = "Test work",
                    dateTime = Instant.now()
                )
            )

            assertEquals(
                worksList[0].id,
                worksList.toEntityList()[0].id
            )
        }
    }

    /**
     * Realiza pruebas unitarias locales de las funciones del archivo
     * WorkWithMeasurementsMappers.kt.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @DisplayName("WorkWithMeasurementsMappers")
    @Nested
    inner class WorkWithMeasurementsMappersTest {
        @DisplayName("Convierte una WorkWithMeasurementsEntity en un modelo de dominio WorkWithMeasurements")
        @Test
        fun workWithMeasurementsEntity_toDomain_isCorrect() {
            val workWithMeasurementsEntity = WorkWithMeasurementsEntity(
                workEntity = WorkEntity(
                    id = 1,
                    type = GarmentType.CAMISA,
                    subtype = GarmentSubType.CAMISA_BASICA,
                    name = "Test work",
                    dateTime = Instant.now()
                ),
                measurementEntities = listOf(
                    MeasurementEntity(
                        id = 1,
                        workId = 1,
                        measurementType = MeasurementType.ANCHO_TELA,
                        value = 150.0
                    )
                )
            )

            assertEquals(
                workWithMeasurementsEntity.workEntity.id,
                workWithMeasurementsEntity.toDomain().work.id
            )
        }
    }
}