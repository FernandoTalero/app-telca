package com.apptelca.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.apptelca.data.DBInfo

/**
 * Clase que modela la relación entre la entidad WorkEntity
 * y varias MeasurementEntity.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class WorkWithMeasurementsEntity(
    @Embedded
    val workEntity: WorkEntity,

    @Relation(
        // Clave primaria
        parentColumn = DBInfo.Work.C_ID,
        // Clave ajena
        entityColumn = DBInfo.Measurement.C_WORK_ID
    )
    val measurementEntities: List<MeasurementEntity>
)