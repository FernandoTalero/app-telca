package com.apptelca.core.di

import android.content.Context
import androidx.room.Room
import com.apptelca.core.sec.EncryptionManager
import com.apptelca.data.DBInfo
import com.apptelca.data.local.dao.MeasurementDao
import com.apptelca.data.local.dao.NoteDao
import com.apptelca.data.local.dao.WorkDao
import com.apptelca.data.local.database.DataBase
import com.apptelca.data.local.database.DbPassphraseHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
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
    fun provideDataBase(
        @ApplicationContext context: Context,
        dbPassphraseHelper: DbPassphraseHelper
    ): DataBase {
        val dbPassphrase = dbPassphraseHelper.getOrCreatePassphrase()
        return Room.databaseBuilder(context.applicationContext, DataBase::class.java, DBInfo.NAME)
            .openHelperFactory(SupportOpenHelperFactory(dbPassphrase))
            .build()
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

    // SecureDBHelper
    @Singleton
    @Provides
    fun providesDbPassphraseHelper(
        @ApplicationContext context: Context,
        encryptionManager: EncryptionManager
    ): DbPassphraseHelper {
        return DbPassphraseHelper(encryptionManager, context)
    }
}