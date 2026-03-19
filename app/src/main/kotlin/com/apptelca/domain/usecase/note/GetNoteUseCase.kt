package com.apptelca.domain.usecase.note

import com.apptelca.domain.model.Note
import com.apptelca.domain.repository.NoteRepository

/**
 * Obtiene un trabajo de la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class GetNoteUseCase(private val noteRepository: NoteRepository) {
    /**
     * Obtiene un objeto Note de la base de datos.
     *
     * @param noteId El id del objeto Note que se desea obtener.
     * @return El objeto Note cuyo id se pasa como argumento.
     */
    suspend operator fun invoke(noteId: Long): Note? {
        return noteRepository.getNote(noteId)
    }
}