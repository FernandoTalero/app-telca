package com.apptelca.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.apptelca.data.DBInfo
import com.apptelca.domain.model.garment.subgarment.MeasurementType

/**
 * Clase entity para las medidas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Entity(
    tableName = DBInfo.Measurement.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = WorkEntity::class,
        parentColumns = [DBInfo.Work.C_ID],
        childColumns = [DBInfo.Measurement.C_WORK_ID],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [DBInfo.Measurement.C_WORK_ID])]
)
data class MeasurementEntity(
    // Clave primaria
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBInfo.Measurement.C_ID)
    var id: Long = 0,

    // Clave ajena
    @ColumnInfo(name = DBInfo.Measurement.C_WORK_ID)
    var workId: Long,

    @ColumnInfo(name = DBInfo.Measurement.C_MEASUREMENT_TYPE)
    var measurementType: MeasurementType,

    @ColumnInfo(name = DBInfo.Measurement.C_VALUE)
    var value: Double
)