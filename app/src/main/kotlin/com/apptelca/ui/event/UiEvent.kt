package com.apptelca.ui.event

/**
 * Clase para notificar los eventos de la UI.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
sealed class UiEvent {
    // Trabajos
    object WorkSaved : UiEvent()
    object WorkUpdated : UiEvent()
    class WorkDeleted(val deletedWorks: Int) : UiEvent()

    // Notas
    object NoteSaved : UiEvent()
    object NoteUpdated : UiEvent()
    class NoteDeleted(val deletedNotes: Int) : UiEvent()

    // Errores
    class Error(val message: String) : UiEvent()
}