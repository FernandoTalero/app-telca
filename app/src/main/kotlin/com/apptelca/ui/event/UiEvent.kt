package com.apptelca.ui.event

/**
 * Interfaz para notificar los eventos de la UI.
 *
 * @author Fernando Talero
 * @version 1.1
 * @since 1.0
 */
sealed interface UiEvent {
    // Trabajos
    data object WorkSaved : UiEvent
    data object WorkUpdated : UiEvent
    data class WorkDeleted(val deletedWorks: Int) : UiEvent

    // Notas
    data object NoteSaved : UiEvent
    data object NoteUpdated : UiEvent
    data class NoteDeleted(val deletedNotes: Int) : UiEvent

    // Errores
    data class Error(val message: String) : UiEvent
}