package com.apptelca.domain.usecase.note

import com.apptelca.domain.model.Note
import com.apptelca.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

/**
 * Obtiene de la base de datos un Flow con una lista de objetos Note.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class GetNotesUseCase(private val noteRepository: NoteRepository) {
    /**
     * Obtiene de la base de datos un Flow con una lista de objetos Note.
     *
     * @param workId El id del trabajo cuyas notas se desean obtener.
     * @return Un Flow con una lista de objetos Note.
     */
    operator fun invoke(workId: Long): Flow<List<Note>> {
        return noteRepository.getNotes(workId)
    }
}