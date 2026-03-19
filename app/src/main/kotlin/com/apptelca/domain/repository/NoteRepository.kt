package com.apptelca.domain.repository

import com.apptelca.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para el repositorio de base de datos para las notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface NoteRepository {
    /**
     * Inserta un objeto Note.
     *
     * @param note El objeto Note a insertar.
     */
    suspend fun insertNote(note: Note)

    /**
     * Actualiza un objeto Note.
     *
     * @param note El objeto Note a actualizar.
     */
    suspend fun updateNote(note: Note)

    /**
     * Elimina uno o más objetos Note.
     *
     * @param notes Lista con los objetos Note a eliminar.
     * @return El número de filas afectadas.
     */
    suspend fun deleteNotes(notes: List<Note>): Int

    /**
     * Obtiene todas las notas de un objeto Work.
     *
     * @param workId El id del objeto Work cuyas notas se desea obtener.
     * @return Un Flow con la lista de notas del objeto Work.
     */
    fun getNotes(workId: Long): Flow<List<Note>>

    /**
     * Obtiene un objeto Note a partir de su id.
     *
     * @param noteId El id del objeto Note a obtener.
     * @return Un objeto Note o null si el id no existe.
     */
    suspend fun getNote(noteId: Long): Note?
}