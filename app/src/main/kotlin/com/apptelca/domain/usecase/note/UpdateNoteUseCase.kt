package com.apptelca.domain.usecase.note

import com.apptelca.domain.model.Note
import com.apptelca.domain.repository.NoteRepository

/**
 * Actualiza un objeto Note en la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class UpdateNoteUseCase(private val noteRepository: NoteRepository) {
    /**
     * Actualiza un objeto Note en la base de datos.
     *
     * @param note El objeto Note a actualizar.
     */
    suspend operator fun invoke(note: Note) {
        noteRepository.updateNote(note)
    }
}