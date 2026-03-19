package com.apptelca.domain.usecase

import com.apptelca.domain.usecase.note.DeleteNotesUseCase
import com.apptelca.domain.usecase.note.GetNoteUseCase
import com.apptelca.domain.usecase.note.GetNotesUseCase
import com.apptelca.domain.usecase.note.SaveNoteUseCase
import com.apptelca.domain.usecase.note.UpdateNoteUseCase
import com.apptelca.domain.usecase.note.ValidateNoteTextUseCase
import com.apptelca.domain.usecase.note.ValidateNoteTextValueUseCase

/**
 * Envuelve todos las funciones de casos de uso para las notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class NoteUseCase(
    val getNoteUseCase: GetNoteUseCase,
    val getNotesUseCase: GetNotesUseCase,
    val saveNoteUseCase: SaveNoteUseCase,
    val updateNoteUseCase: UpdateNoteUseCase,
    val deleteNotesUseCase: DeleteNotesUseCase,
    val validateNoteTextValueUseCase: ValidateNoteTextValueUseCase,
    val validateNoteTextUseCase: ValidateNoteTextUseCase
)