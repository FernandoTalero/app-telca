package com.apptelca.domain.repository

import com.apptelca.domain.model.Measurement

/**
 * Interfaz para el repositorio de base de datos para las medidas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface MeasurementRepository {
    /**
     * Inserta un objeto Measurement.
     *
     * @param measurements Lista con los objetos Measurement a insertar.
     */
    suspend fun insertMeasurements(measurements: List<Measurement>)

    /**
     * Actualiza uno o varios objetos Measurement.
     *
     * @param measurements Lista con los objetos Measurement a actualizar.
     */
    suspend fun updateMeasurements(measurements: List<Measurement>)
}