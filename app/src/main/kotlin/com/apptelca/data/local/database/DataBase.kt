package com.apptelca.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apptelca.data.DBInfo
import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.local.dao.NoteDao
import com.apptelca.data.local.dao.WorkDao
import com.apptelca.data.local.entities.MeasurementEntity
import com.apptelca.data.local.entities.NoteEntity
import com.apptelca.data.local.entities.WorkEntity
import com.apptelca.data.mapper.DataBaseConverter

/**
 * Clase para la instanciación de la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Database(
    entities = [WorkEntity::class, MeasurementEntity::class, NoteEntity::class],
    version = DBInfo.VERSION,
    exportSchema = false
)
@TypeConverters(DataBaseConverter::class)
abstract class DataBase : RoomDatabase() {
    // Funciones para obtener los DAO
    abstract fun workDao(): WorkDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun noteDao(): NoteDao
}