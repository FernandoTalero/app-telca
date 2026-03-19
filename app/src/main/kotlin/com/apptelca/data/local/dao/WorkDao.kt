package com.apptelca.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.apptelca.data.DBInfo
import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.data.local.entities.WorkWithMeasurementsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz Dao para WorkEntity.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Dao
interface WorkDao {
    /**
     * Inserta una WorkEntity.
     *
     * @param workEntity La WorkEntity a insertar.
     * @return El id de la WorkEntity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(workEntity: WorkEntity): Long

    /**
     * Actualiza una WorkEntity.
     *
     * @param workEntity La WorkEntity a actualizar.
     */
    @Update
    suspend fun updateWork(workEntity: WorkEntity)

    /**
     * Elimina una o varias WorkEntity.
     *
     * @param workEntities Lista con las WorkEntity a eliminar.
     * @return El número de registros afectados.
     */
    @Delete
    suspend fun deleteWorks(workEntities: List<WorkEntity>): Int
    /**
     * Obtiene todas las WorkEntity ordenadas descendentemente.
     *
     * @return Un Flow con la lista de todas las WorkEntity.
     */
    @Query("SELECT * FROM ${DBInfo.Work.TABLE_NAME} ORDER BY date_time DESC")
    fun getWorks(): Flow<List<WorkEntity>>

    /**
     * Obtiene una WorkEntity a partir de su id.
     *
     * @param workId El id de la WorkEntity a obtener.
     * @return Una WorkEntity o null si el id no existe.
     */
    @Query("SELECT * FROM ${DBInfo.Work.TABLE_NAME} WHERE ${DBInfo.Work.C_ID} = :workId")
    suspend fun getWork(workId: Long): WorkEntity?

    /**
     * Obtiene un objeto WorkWithMeasurementsEntity.
     *
     * @param workId El id de la WorkEntity a obtener.
     * @return Un objeto WorkWithMeasurementsEntity o null si el id no existe.
     */
    @Transaction
    @Query("SELECT * FROM ${DBInfo.Work.TABLE_NAME} WHERE ${DBInfo.Work.C_ID} = :workId")
    suspend fun getWorkWithMeasurements(workId: Long): WorkWithMeasurementsEntity?
}