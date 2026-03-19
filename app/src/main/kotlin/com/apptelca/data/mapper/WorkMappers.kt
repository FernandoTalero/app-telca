package com.apptelca.data.mapper

import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.domain.model.Work

/**
 * Convierte una WorkEntity en un modelo de dominio Work.
 *
 * @return Un modelo de dominio Work.
 */
fun WorkEntity.toDomain(): Work {
    return Work(
        id = id,
        type = type,
        subtype = subtype,
        name = name,
        dateTime = dateTime
    )
}

/**
 * Convierte una lista de WorkEntity en una lista de modelos de dominio Work.
 *
 * @return Una lista de modelos de dominio Work.
 */
fun List<WorkEntity>.toDomainList(): List<Work> {
    return this.map { it.toDomain() }
}

/**
 * Convierte un modelo de dominio Work en una WorkEntity.
 *
 * @return Una WorkEntity.
 */
fun Work.toEntity(): WorkEntity {
    return WorkEntity(
        id = id,
        type = type,
        subtype = subtype,
        name = name,
        dateTime = dateTime
    )
}

/**
 * Convierte una lista de modelos de dominio Work en una lista de WorkEntity.
 *
 * @return Una lista de WorkEntity.
 */
fun List<Work>.toEntityList(): List<WorkEntity> {
    return this.map { it.toEntity() }
}