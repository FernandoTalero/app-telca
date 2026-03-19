package com.apptelca.domain.usecase.note

import com.apptelca.domain.model.Note
import com.apptelca.domain.repository.NoteRepository

/**
 * Elimina una lista de notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class DeleteNotesUseCase(private val noteRepository: NoteRepository) {
    /**
     * Elimina una lista de notas.
     *
     * @return El número de notas eliminadas.
     */
    suspend operator fun invoke(notesToDelete: List<Note>): Int {
        return noteRepository.deleteNotes(notesToDelete)
    }
}