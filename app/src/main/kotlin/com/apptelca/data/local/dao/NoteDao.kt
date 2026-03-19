package com.apptelca.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apptelca.data.DBInfo
import com.apptelca.data.local.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz Dao para NoteEntity.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Dao
interface NoteDao {
    /**
     * Inserta una NoteEntity.
     *
     * @param noteEntity La NoteEntity a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity)

    /**
     * Actualiza una NoteEntity.
     *
     * @param noteEntity La NoteEntity a actualizar.
     */
    @Update
    suspend fun updateNote(noteEntity: NoteEntity)

    /**
     * Elimina una o más NoteEntity.
     *
     * @param noteEntities Lista de NoteEntity a eliminar.
     * @return El número de registros afectados.
     */
    @Delete
    suspend fun deleteNotes(noteEntities: List<NoteEntity>): Int

    /**
     * Obtiene todas las NoteEntity de una WorkEntity.
     *
     * @param workId El id de la WorkEntity cuyas NoteEntity se desea obtener.
     * @return Un Flow con la lista de NoteEntity de la WorkEntity.
     */
    @Query("SELECT * FROM ${DBInfo.Note.TABLE_NAME} WHERE " +
            "${DBInfo.Note.C_WORK_ID} = :workId ORDER BY ${DBInfo.Note.C_DATE_TIME} DESC")
    fun getNotes(workId: Long): Flow<List<NoteEntity>>

    /**
     * Obtiene una NoteEntity a partir de su id.
     *
     * @param noteId El id de la NoteEntity a obtener.
     * @return Una NoteEntity o null si el id no existe.
     */
    @Query("SELECT * FROM ${DBInfo.Note.TABLE_NAME} WHERE ${DBInfo.Note.C_ID} = :noteId")
    suspend fun getNote(noteId: Long): NoteEntity?
}