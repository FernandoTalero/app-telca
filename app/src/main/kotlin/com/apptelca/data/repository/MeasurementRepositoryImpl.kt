package com.apptelca.data.repository

import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.mapper.toEntityList
import com.apptelca.di.IoDispatcher
import com.apptelca.domain.model.Measurement
import com.apptelca.domain.repository.MeasurementRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio de base de datos para las entidades Measurement.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class MeasurementRepositoryImpl @Inject constructor(
    private val measurementDao: MeasurementDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MeasurementRepository {
    /**
     * Inserta uno o varios objetos Measurement.
     *
     * @param measurements Lista con los objetos Measurement a insertar.
     */
    override suspend fun insertMeasurements(measurements: List<Measurement>) =
        withContext(ioDispatcher) {
            measurementDao.insertMeasurements(measurements.toEntityList())
        }

    /**
     * Actualiza uno o varios objetos Measurement.
     *
     * @param measurements Lista con los objetos Measurement a actualizar.
     */
    override suspend fun updateMeasurements(measurements: List<Measurement>) =
        withContext(ioDispatcher) {
            measurementDao.updateMeasurements(measurements.toEntityList())
        }
}