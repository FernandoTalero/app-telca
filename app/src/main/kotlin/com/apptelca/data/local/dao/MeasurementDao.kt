package com.apptelca.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apptelca.data.local.entities.MeasurementEntity

/**
 * Interfaz Dao para MeasurementEntity.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Dao
interface MeasurementDao {
    /**
     * Inserta una o varias MeasurementEntity.
     *
     * @param measurementEntities Lista con las MeasurementEntity a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurements(measurementEntities: List<MeasurementEntity>)

    /**
     * Actualiza una o varias MeasurementEntity.
     *
     * @param measurementEntities Lista con las MeasurementEntity a actualizar.
     */
    @Update
    suspend fun updateMeasurements(measurementEntities: List<MeasurementEntity>)

    /**
     * Obtiene una lista con las MeasurementEntity de un trabajo.
     *
     * @param workId El id del trabajo cuyas MeasurementEntity se quieren obtener.
     */
    @Query("SELECT * FROM measurements WHERE work_id = :workId")
    suspend fun getMeasurements(workId: Long): List<MeasurementEntity>
}