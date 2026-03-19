package com.apptelca.data.repository

import com.apptelca.data.local.dao.NoteDao
import com.apptelca.data.mapper.toDomain
import com.apptelca.data.mapper.toDomainList
import com.apptelca.data.mapper.toEntity
import com.apptelca.data.mapper.toEntityList
import com.apptelca.di.DefaultDispatcher
import com.apptelca.di.IoDispatcher
import com.apptelca.domain.model.Note
import com.apptelca.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repositorio de base de datos para las notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NoteRepository {
    /**
     * Inserta un objeto Note.
     *
     * @param note El objeto Note a insertar.
     */
    override suspend fun insertNote(note: Note) = withContext(ioDispatcher) {
        noteDao.insertNote(note.toEntity())
    }

    /**
     * Actualiza un objeto Note.
     *
     * @param note El objeto Note a actualizar.
     */
    override suspend fun updateNote(note: Note) = withContext(ioDispatcher) {
        noteDao.updateNote(note.toEntity())
    }

    /**
     * Elimina uno o más objetos Note.
     *
     * @param notes Lista con los objetos Note a eliminar.
     * @return El número de filas afectadas.
     */
    override suspend fun deleteNotes(notes: List<Note>): Int = withContext(ioDispatcher) {
        noteDao.deleteNotes(notes.toEntityList())
    }

    /**
     * Obtiene todas las notas de un objeto Work.
     *
     * @param workId El id del objeto Work cuyas notas se desea obtener.
     * @return Un Flow con la lista de notas del objeto Work.
     */
    override fun getNotes(workId: Long): Flow<List<Note>> =
        noteDao.getNotes(workId).map { entities -> entities.toDomainList() }
            .flowOn(defaultDispatcher)

    /**
     * Obtiene un objeto Note a partir de su id.
     *
     * @param noteId El id del objeto Note a obtener.
     * @return Un objeto Note o null si el id no existe.
     */
    override suspend fun getNote(noteId: Long): Note? =
        withContext(ioDispatcher) {
            noteDao.getNote(noteId)?.toDomain()
        }
}