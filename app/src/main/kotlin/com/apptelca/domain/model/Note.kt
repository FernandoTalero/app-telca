package com.apptelca.domain.model

import java.time.Instant

/**
 * Clase modelo de dominio para una nota.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class Note(
    val id: Long = 0,
    val workId: Long,
    val text: String,
    val dateTime: Instant
)