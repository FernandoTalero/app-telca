package com.apptelca.data.repository

import androidx.room.withTransaction
import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.local.dao.WorkDao
import com.apptelca.data.local.database.DataBase
import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.data.mapper.toDomain
import com.apptelca.data.mapper.toDomainList
import com.apptelca.data.mapper.toEntity
import com.apptelca.data.mapper.toEntityList
import com.apptelca.di.DefaultDispatcher
import com.apptelca.di.IoDispatcher
import com.apptelca.domain.model.Work
import com.apptelca.domain.model.WorkWithMeasurements
import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import com.apptelca.domain.repository.WorkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio de base de datos para los trabajos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class WorkRepositoryImpl @Inject constructor(
    private val dataBase: DataBase,
    private val workDao: WorkDao,
    private val measurementDao: MeasurementDao,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WorkRepository {
    /**
     * Inserta un objeto Work junto con las medidas correspondientes.
     *
     * @param work El objeto Work a insertar.
     * @param measurementsValues Lista con las medidas.
     * @return El id del trabajo almacenado.
     */
    override suspend fun insertWork(work: Work, measurementsValues: List<MeasurementValue>) =
        withContext(ioDispatcher) {
            dataBase.withTransaction {
                val workId = workDao.insertWork(work.toEntity())
                val measurementEntities = measurementsValues.map {
                    MeasurementEntity(
                        workId = workId,
                        measurementType = it.measurementType,
                        value = it.value
                    )
                }
                measurementDao.insertMeasurements(measurementEntities)
                workId
            }
        }

    /**
     * Actualiza un objeto Work junto con las medidas correspondientes.
     *
     * @param work El objeto Work a actualizar.
     * @param measurementsValues Lista con las medidas.
     */
    override suspend fun updateWork(work: Work, measurementsValues: List<MeasurementValue>) =
        withContext(ioDispatcher) {
            dataBase.withTransaction {
                workDao.updateWork(work.toEntity())

                val measurements = measurementDao.getMeasurements(work.toEntity().id)
                val measurementsValuesMap = measurementsValues.associateBy { it.measurementType }

                val updatedMeasurements = measurements.map { measurement ->
                    measurement.copy(
                        value = measurementsValuesMap[measurement.measurementType]?.value
                            ?: measurement.value
                    )
                }
                measurementDao.updateMeasurements(updatedMeasurements)
            }
        }

    /**
     * Elimina uno o varios objetos Work.
     *
     * @param works Lista con los objetos Work a eliminar.
     */
    override suspend fun deleteWorks(works: List<Work>) = withContext(ioDispatcher) {
        workDao.deleteWorks(works.toEntityList())
    }

    /**
     * Obtiene todas los objetos Work.
     *
     * @return Un Flow con una lista de todos los objetos Work.
     */
    override fun getWorks(): Flow<List<Work>> =
        workDao.getWorks().map { entities -> entities.toDomainList() }
            .flowOn(defaultDispatcher)

    /**
     * Obtiene un objeto Work a partir de su id.
     *
     * @param workId El id del objeto Work a obtener.
     * @return Un objeto Work o null si el id no existe.
     */
    override suspend fun getWork(workId: Long): Work? = withContext(ioDispatcher) {
        workDao.getWork(workId)?.toDomain()
    }

    /**
     * Obtiene un objeto WorkWithMeasurements.
     *
     * @param workId El id del objeto Work a obtener.
     * @return Un objeto WorkWithMeasurements o null si el id no existe.
     */
    override suspend fun getWorkWithMeasurements(workId: Long): WorkWithMeasurements? =
        withContext(ioDispatcher) {
            workDao.getWorkWithMeasurements(workId)?.toDomain()
        }
}