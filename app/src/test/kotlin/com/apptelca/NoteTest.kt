package com.apptelca

import com.apptelca.data.local.entities.NoteEntity
import com.apptelca.data.mapper.toDomain
import com.apptelca.data.mapper.toDomainList
import com.apptelca.data.mapper.toEntity
import com.apptelca.data.mapper.toEntityList
import com.apptelca.domain.model.Note
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 * pruebas unitarias locales de funciones para notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */

@DisplayName("Pruebas unitarias locales de funciones para notas")
class NoteTest {
    /**
     * Realiza pruebas unitarias locales de las funciones del archivo
     * NoteMappers.kt.
     *
     * @author Fernando Talero
     * @version 1.0
     * @since 1.0
     */
    @DisplayName("NoteMappers")
    @Nested
    inner class NoteMappersTest {
        @DisplayName("Convierte una NoteEntity en un modelo de dominio Note")
        @Test
        fun noteEntity_toDomain_isCorrect() {
            val noteEntity = NoteEntity(
                id = 1,
                workId = 1,
                text = "",
                dateTime = Instant.now()
            )

            assertEquals(noteEntity.id, noteEntity.toDomain().id)
        }

        @DisplayName("Convierte una lista de NoteEntity en una lista de modelos de dominio Note")
        @Test
        fun noteEntitiesList_toDomainList_isCorrect() {
            val noteEntitiesList = listOf(
                NoteEntity(
                    id = 1,
                    workId = 1,
                    text = "",
                    dateTime = Instant.now()
                )
            )

            assertEquals(
                noteEntitiesList[0].id,
                noteEntitiesList.toDomainList()[0].id
            )
        }

        @DisplayName("Convierte un modelo de dominio Note en una NoteEntity")
        @Test
        fun note_toEntity_isCorrect() {
            val note = Note(
                id = 1,
                workId = 1,
                text = "",
                dateTime = Instant.now()
            )

            assertEquals(note.id, note.toEntity().id)
        }

        @DisplayName("Convierte una lista de modelos de dominio Note en una lista de NoteEntity")
        @Test
        fun notesList_toEntityList_isCorrect() {
            val notesList = listOf(
                Note(
                    id = 1,
                    workId = 1,
                    text = "",
                    dateTime = Instant.now()
                )
            )

            assertEquals(
                notesList[0].id,
                notesList.toEntityList()[0].id
            )
        }
    }
}