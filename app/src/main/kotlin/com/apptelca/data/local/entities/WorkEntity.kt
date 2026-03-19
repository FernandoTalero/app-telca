package com.apptelca.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apptelca.data.DBInfo
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import java.time.Instant

/**
 * Clase entity para los trabajos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Entity(tableName = DBInfo.Work.TABLE_NAME)
data class WorkEntity(
    // Clave primaria
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBInfo.Work.C_ID)
    var id: Long = 0,

    @ColumnInfo(name = DBInfo.Work.C_TYPE)
    var type: GarmentType,

    @ColumnInfo(name = DBInfo.Work.C_SUBTYPE)
    var subtype: GarmentSubType,

    @ColumnInfo(name = DBInfo.Work.C_NAME)
    var name: String,

    @ColumnInfo(name = DBInfo.Work.C_DATE_TIME)
    var dateTime: Instant
)