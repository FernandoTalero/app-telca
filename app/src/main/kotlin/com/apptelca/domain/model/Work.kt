package com.apptelca.domain.model

import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import java.time.Instant

/**
 * Clase modelo de dominio para un trabajo.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class Work(
    val id: Long = 0,
    val type: GarmentType,
    val subtype: GarmentSubType,
    val name: String,
    val dateTime: Instant
)