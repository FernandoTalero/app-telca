package com.apptelca

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.local.dao.WorkDao
import com.apptelca.data.local.database.DataBase
import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

/**
 * Realiza pruebas unitarias instrumentadas para la interfaz MeasurementDao.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@RunWith(AndroidJUnit4::class)
class MeasurementDaoTest {
    private lateinit var dataBase: DataBase
    private lateinit var workDao: WorkDao
    private lateinit var measurementDao: MeasurementDao

    @Before
    fun createDataBase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, DataBase::class.java).build()
        workDao = dataBase.workDao()
        measurementDao = dataBase.measurementDao()
    }

    @After
    fun closeDataBase() {
        dataBase.close()
    }

    @Test
    fun insertMeasurements_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val measurements = listOf(
            MeasurementEntity(
                workId = workId,
                measurementType = MeasurementType.ANCHO_TELA,
                value = 150.0
            )
        )

        // Almacenamos el objeto MeasurementEntity
        measurementDao.insertMeasurements(measurements)
        // Obtenemos el objeto MeasurementEntity almacenado
        val retrievedMeasurement = measurementDao.getMeasurements(workId).first()

        assertNotNull(retrievedMeasurement)
    }

    @Test
    fun updateMeasurements_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val measurements = listOf(
            MeasurementEntity(
                workId = workId,
                measurementType = MeasurementType.ANCHO_TELA,
                value = 100.0
            )
        )

        // Almacenamos el objeto MeasurementEntity
        measurementDao.insertMeasurements(measurements)
        // Obtenemos el objeto MeasurementEntity almacenado
        val retrievedMeasurements = measurementDao.getMeasurements(workId)

        assertTrue(retrievedMeasurements.isNotEmpty())
        if(retrievedMeasurements.isNotEmpty()) {
            // Actualizamos el objeto MeasurementEntity
            val updatedMeasurement = retrievedMeasurements[0].copy(value = 150.0)
            measurementDao.updateMeasurements(listOf(updatedMeasurement))

            // Obtenemos el objeto MeasurementEntity actualizado
            val retrievedUpdatedMeasurement = measurementDao.getMeasurements(workId)[0]

            assertNotNull(retrievedUpdatedMeasurement)
            assertEquals(150.0, retrievedUpdatedMeasurement.value, 0.0)
        }
    }

    @Test
    fun getMeasurements_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)

        val measurements = listOf(
            MeasurementEntity(
                workId = workId,
                measurementType = MeasurementType.ANCHO_TELA,
                value = 100.0
            )
        )

        // Almacenamos el objeto MeasurementEntity
        measurementDao.insertMeasurements(measurements)
        // Obtenemos el objeto MeasurementEntity almacenado
        val retrievedMeasurements = measurementDao.getMeasurements(workId)

        assertTrue(retrievedMeasurements.isNotEmpty())
        if(retrievedMeasurements.isNotEmpty()) {
            assertNotNull(retrievedMeasurements[0])
        }
    }
}