package com.apptelca.di

import com.apptelca.core.ResourceProvider
import com.apptelca.validator.MeasurementValidator
import com.apptelca.validator.MeasurementValueValidator
import com.apptelca.validator.NoteTextValidator
import com.apptelca.validator.NoteTextValueValidator
import com.apptelca.validator.WorkNameValidator
import com.apptelca.validator.WorkNameValueValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la inyección de dependencias de objetos Validator.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object ValidatorModule {
    @Singleton
    @Provides
    fun provideMeasurementValueValidator(resourceProvider: ResourceProvider): MeasurementValueValidator {
        return MeasurementValueValidator(resourceProvider)
    }

    @Singleton
    @Provides
    fun provideMeasurementValidator(resourceProvider: ResourceProvider): MeasurementValidator {
        return MeasurementValidator(resourceProvider)
    }

    @Singleton
    @Provides
    fun provideWorkNameValueValidator(): WorkNameValueValidator {
        return WorkNameValueValidator()
    }

    @Singleton
    @Provides
    fun provideWorkNameValidator(): WorkNameValidator {
        return WorkNameValidator()
    }

    @Singleton
    @Provides
    fun provideNoteTextValueValidator(): NoteTextValueValidator {
        return NoteTextValueValidator()
    }

    @Singleton
    @Provides
    fun provideNoteTextValidator(): NoteTextValidator {
        return NoteTextValidator()
    }
}