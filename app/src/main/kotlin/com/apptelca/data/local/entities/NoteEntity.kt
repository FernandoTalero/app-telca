package com.apptelca.data.local.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.apptelca.data.DBInfo
import kotlinx.parcelize.Parcelize
import java.time.Instant

/**
 * Clase entity para las notas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Parcelize
@Entity(
    tableName = DBInfo.Note.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = WorkEntity::class,
        parentColumns = [DBInfo.Work.C_ID],
        childColumns = [DBInfo.Note.C_WORK_ID],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [DBInfo.Note.C_WORK_ID])]
)
data class NoteEntity(
    // Clave primaria
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBInfo.Note.C_ID)
    var id: Long = 0,

    // Clave ajena
    @ColumnInfo(name = DBInfo.Note.C_WORK_ID)
    var workId: Long,

    @ColumnInfo(name = DBInfo.Note.C_TEXT)
    var text: String,

    @ColumnInfo(name = DBInfo.Note.C_DATE_TIME)
    var dateTime: Instant
) : Parcelable