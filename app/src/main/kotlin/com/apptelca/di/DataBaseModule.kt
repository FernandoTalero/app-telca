package com.apptelca.di

import android.content.Context
import androidx.room.Room
import com.apptelca.data.DBInfo
import com.apptelca.data.local.database.DataBase
import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.local.dao.NoteDao
import com.apptelca.data.local.dao.WorkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la inyección de dependencias de objetos de
 * la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    // DataBase
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            DataBase::class.java,
            DBInfo.NAME
        ).build()
    }

    // WorkDao
    @Singleton
    @Provides
    fun provideWorkDao(dataBase: DataBase): WorkDao {
        return dataBase.workDao()
    }

    // MeasurementDao
    @Singleton
    @Provides
    fun provideMeasurementDao(dataBase: DataBase): MeasurementDao {
        return dataBase.measurementDao()
    }

    // NoteDao
    @Singleton
    @Provides
    fun provideNoteDao(dataBase: DataBase): NoteDao {
        return dataBase.noteDao()
    }
}