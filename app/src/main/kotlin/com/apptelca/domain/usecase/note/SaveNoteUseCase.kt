package com.apptelca.domain.usecase.note

import com.apptelca.domain.model.Note
import com.apptelca.domain.repository.NoteRepository

/**
 * Almacena un objeto Note en la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class SaveNoteUseCase(private val noteRepository: NoteRepository) {
    /**
     * Almacena un objeto Note en la base de datos.
     *
     * @param note El objeto Note a almacenar.
     */
    suspend operator fun invoke(note: Note) {
        noteRepository.insertNote(note)
    }
}