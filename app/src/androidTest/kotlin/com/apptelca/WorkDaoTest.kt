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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

/**
 * Realiza pruebas unitarias instrumentadas para la interfaz WorkDao.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@RunWith(AndroidJUnit4::class)
class WorkDaoTest {
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
    fun insertWork_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)
        // Obtenemos el objeto WorkEntity almacenado
        val retrievedWorkEntity = workDao.getWork(workId)

        requireNotNull(retrievedWorkEntity)
        assertEquals(workId, retrievedWorkEntity.id)
    }

    @Test
    fun updateWork_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)
        // Obtenemos el objeto WorkEntity almacenado
        val retrievedWorkEntity = workDao.getWork(workId)

        // Actualizamos el objeto WorkEntity
        val updatedWorkEntity = retrievedWorkEntity?.copy(name = "Updated test work")
        updatedWorkEntity?.let { workDao.updateWork(it) }

        // Obtenemos el objeto WorkEntity actualizado
        val retrievedUpdatedWorkEntity = workDao.getWork(workId)

        requireNotNull(retrievedUpdatedWorkEntity)
        assertEquals("Updated test work", retrievedUpdatedWorkEntity.name)
    }

    @Test
    fun deleteWorks_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)
        // Obtenemos el objeto WorkEntity almacenado
        val retrievedWorkEntity = workDao.getWork(workId)

        requireNotNull(retrievedWorkEntity)
        val worksList = listOf(retrievedWorkEntity)

        // Eliminamos el objeto WorkEntity almacenado
        workDao.deleteWorks(worksList)

        val deletedWorkEntity = workDao.getWork(workId)
        assertNull(deletedWorkEntity)
    }

    @Test
    fun getWorks_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        workDao.insertWork(workEntity)
        // Obtenemos una lista de objetos WorkEntity a partir del Flow correspondiente
        val retrievedWorkEntityList = workDao.getWorks().first()

        assertEquals(1, retrievedWorkEntityList.size)
    }

    @Test
    fun getWork_isCorrect() = runTest {
        val workEntity = WorkEntity(
            type = GarmentType.CAMISA,
            subtype = GarmentSubType.CAMISA_BASICA,
            name = "Test work",
            dateTime = Instant.now()
        )

        // Almacenamos el objeto WorkEntity
        val workId = workDao.insertWork(workEntity)
        // Obtenemos el objeto WorkEntity almacenado
        val retrievedWorkEntity = workDao.getWork(workId)

        assertNotNull(retrievedWorkEntity)
    }

    @Test
    fun getWorkWithMeasurements_isCorrect() = runTest {
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

        val workWithMeasurementsEntity = workDao.getWorkWithMeasurements(workId)
        assertNotNull(workWithMeasurementsEntity)
    }
}