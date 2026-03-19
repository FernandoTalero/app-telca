package com.apptelca.domain.repository

import com.apptelca.domain.model.Work
import com.apptelca.domain.model.WorkWithMeasurements
import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para el repositorio de base de datos para los trabajos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface WorkRepository {
    /**
     * Inserta un objeto Work junto con las medidas correspondientes.
     *
     * @param work Objeto Work a insertar.
     * @param measurementsValues Lista con las medidas.
     * @return El id del trabajo almacenado
     */
    suspend fun insertWork(work: Work, measurementsValues: List<MeasurementValue>): Long

    /**
     * Actualiza un objeto Work junto con las medidas correspondientes.
     *
     * @param work El objeto Work a actualizar.
     * @param measurementsValues Lista con las medidas.
     */
    suspend fun updateWork(work: Work, measurementsValues: List<MeasurementValue>)

    /**
     * Elimina uno o varios objetos Work.
     *
     * @param works Lista con los objetos Work a eliminar.
     * @return El número de filas afectadas.
     */
    suspend fun deleteWorks(works: List<Work>): Int

    /**
     * Obtiene todos los objetos Work.
     *
     * @return Un Flow con una lista de todos los objetos Work.
     */
    fun getWorks(): Flow<List<Work>>

    /**
     * Obtiene un objeto Work a partir de su id.
     *
     * @param workId El id del objeto Work a obtener.
     * @return Un objeto Work o null si el id no existe.
     */
    suspend fun getWork(workId: Long): Work?

    /**
     * Obtiene un objeto Work junto con sus objetos Measurement.
     *
     * @param workId El id del objeto Work a obtener.
     * @return Un objeto Work junto con sus objetos Measurement o null
     * si el id no existe.
     */
    suspend fun getWorkWithMeasurements(workId: Long): WorkWithMeasurements?
}