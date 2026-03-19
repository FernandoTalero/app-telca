package com.apptelca.data.mapper

import com.apptelca.data.local.entities.NoteEntity
import com.apptelca.domain.model.Note

/**
 * Convierte una NoteEntity en un modelo de dominio Note.
 *
 * @return Un modelo de dominio Note.
 */
fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        workId = workId,
        text = text,
        dateTime = dateTime
    )
}

/**
 * Convierte una lista de NoteEntity en una lista de modelos de dominio Note.
 *
 * @return Una lista de modelos de dominio Note.
 */
fun List<NoteEntity>.toDomainList(): List<Note> {
    return this.map { it.toDomain() }
}

/**
 * Convierte un modelo de dominio Note en una NoteEntity.
 *
 * @return Una NoteEntity.
 */
fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        workId = workId,
        text = text,
        dateTime = dateTime
    )
}

/**
 * Convierte una lista de modelos de dominio Note en una lista de NoteEntity.
 *
 * @return Una lista de NoteEntity.
 */
fun List<Note>.toEntityList(): List<NoteEntity> {
    return this.map { it.toEntity() }
}