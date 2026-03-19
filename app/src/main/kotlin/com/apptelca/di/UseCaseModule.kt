package com.apptelca.di

import com.apptelca.domain.repository.NoteRepository
import com.apptelca.domain.repository.WorkRepository
import com.apptelca.domain.usecase.NoteUseCase
import com.apptelca.domain.usecase.SavedWorkUseCase
import com.apptelca.domain.usecase.WorkUseCase
import com.apptelca.domain.usecase.note.DeleteNotesUseCase
import com.apptelca.domain.usecase.note.GetNoteUseCase
import com.apptelca.domain.usecase.note.GetNotesUseCase
import com.apptelca.domain.usecase.note.SaveNoteUseCase
import com.apptelca.domain.usecase.note.UpdateNoteUseCase
import com.apptelca.domain.usecase.note.ValidateNoteTextUseCase
import com.apptelca.domain.usecase.note.ValidateNoteTextValueUseCase
import com.apptelca.domain.usecase.savedwork.DeleteWorksUseCase
import com.apptelca.domain.usecase.savedwork.GetWorksUseCase
import com.apptelca.domain.usecase.work.GetWorkUseCase
import com.apptelca.domain.usecase.work.GetWorkWithMeasurementsUseCase
import com.apptelca.domain.usecase.work.SaveWorkUseCase
import com.apptelca.domain.usecase.work.UpdateWorkUseCase
import com.apptelca.domain.usecase.work.ValidateMeasurementUseCase
import com.apptelca.domain.usecase.work.ValidateMeasurementValueUseCase
import com.apptelca.domain.usecase.work.ValidateWorkNameUseCase
import com.apptelca.domain.usecase.work.ValidateWorkNameValueUseCase
import com.apptelca.validator.MeasurementValidator
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
 * Módulo de Hilt para la inyección de dependencias de objetos UseCase.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // WORK
    @Singleton
    @Provides
    fun provideGetWorkWithMeasurementsUseCase(workRepository: WorkRepository):
            GetWorkWithMeasurementsUseCase {
        return GetWorkWithMeasurementsUseCase(workRepository)
    }

    @Singleton
    @Provides
    fun provideValidateMeasurementUseCase(validator: MeasurementValidator):
            ValidateMeasurementUseCase {
        return ValidateMeasurementUseCase(validator)
    }

    @Singleton
    @Provides
    fun provideValidateWorkNameValueUseCase(validator: WorkNameValueValidator):
            ValidateWorkNameValueUseCase {
        return ValidateWorkNameValueUseCase(validator)
    }

    @Singleton
    @Provides
    fun provideValidateWorkNameUseCase(validator: WorkNameValidator): ValidateWorkNameUseCase {
        return ValidateWorkNameUseCase(validator)
    }

    @Singleton
    @Provides
    fun provideGetWorkUseCase(workRepository: WorkRepository): GetWorkUseCase {
        return GetWorkUseCase(workRepository)
    }

    @Singleton
    @Provides
    fun provideSaveWorkUseCase(workRepository: WorkRepository): SaveWorkUseCase {
        return SaveWorkUseCase(workRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateWorkUseCase(workRepository: WorkRepository): UpdateWorkUseCase {
        return UpdateWorkUseCase(workRepository)
    }

    @Singleton
    @Provides
    fun provideWorkUseCases(
        getWorkWithMeasurementsUseCase: GetWorkWithMeasurementsUseCase,
        getWorkUseCase: GetWorkUseCase,
        saveWorkUseCase: SaveWorkUseCase,
        updateWorkUseCase: UpdateWorkUseCase,
        validateMeasurementValueUseCase: ValidateMeasurementValueUseCase,
        validateMeasurementUseCase: ValidateMeasurementUseCase,
        validateWorkNameValueUseCase: ValidateWorkNameValueUseCase,
        validateWorkNameUseCase: ValidateWorkNameUseCase,

    ): WorkUseCase {
        return WorkUseCase(
            getWorkWithMeasurementsUseCase = getWorkWithMeasurementsUseCase,
            getWorkUseCase = getWorkUseCase,
            saveWorkUseCase = saveWorkUseCase,
            updateWorkUseCase = updateWorkUseCase,
            validateMeasurementValueUseCase = validateMeasurementValueUseCase,
            validateMeasurementUseCase = validateMeasurementUseCase,
            validateWorkNameValueUseCase = validateWorkNameValueUseCase,
            validateWorkNameUseCase = validateWorkNameUseCase,
        )
    }

    // NOTE
    @Singleton
    @Provides
    fun provideGetNoteUseCase(noteRepository: NoteRepository): GetNoteUseCase {
        return GetNoteUseCase(noteRepository)
    }

    @Singleton
    @Provides
    fun provideGetNotesUseCase(noteRepository: NoteRepository): GetNotesUseCase {
        return GetNotesUseCase(noteRepository)
    }

    @Singleton
    @Provides
    fun provideSaveNoteUseCase(noteRepository: NoteRepository): SaveNoteUseCase {
        return SaveNoteUseCase(noteRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateNoteUseCase(noteRepository: NoteRepository): UpdateNoteUseCase {
        return UpdateNoteUseCase(noteRepository)
    }


    @Singleton
    @Provides
    fun provideDeleteNotesUseCase(noteRepository: NoteRepository): DeleteNotesUseCase {
        return DeleteNotesUseCase(noteRepository)
    }

    @Singleton
    @Provides
    fun provideValidateNoteTextUseCase(noteTextValidator: NoteTextValidator): ValidateNoteTextUseCase {
        return ValidateNoteTextUseCase(noteTextValidator)
    }

    @Singleton
    @Provides
    fun provideValidateNoteTextValueUseCase(noteTextValueValidator: NoteTextValueValidator):
            ValidateNoteTextValueUseCase {
        return ValidateNoteTextValueUseCase(noteTextValueValidator)
    }

    @Singleton
    @Provides
    fun provideNoteUseCase(
        getNoteUseCase: GetNoteUseCase,
        getNotesUseCase: GetNotesUseCase,
        saveNoteUseCase: SaveNoteUseCase,
        updateNoteUseCase: UpdateNoteUseCase,
        deleteNotesUseCase: DeleteNotesUseCase,
        validateNoteTextValueUseCase: ValidateNoteTextValueUseCase,
        validateNoteTextUseCase: ValidateNoteTextUseCase,
    ): NoteUseCase {
        return NoteUseCase(
            getNoteUseCase = getNoteUseCase,
            getNotesUseCase = getNotesUseCase,
            saveNoteUseCase = saveNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            deleteNotesUseCase = deleteNotesUseCase,
            validateNoteTextValueUseCase = validateNoteTextValueUseCase,
            validateNoteTextUseCase = validateNoteTextUseCase,
        )
    }

    // SAVED WORK
    @Singleton
    @Provides
    fun provideGetWorksUseCase(workRepository: WorkRepository): GetWorksUseCase {
        return GetWorksUseCase(workRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteWorksUseCase(workRepository: WorkRepository): DeleteWorksUseCase {
        return DeleteWorksUseCase(workRepository)
    }

    @Singleton
    @Provides
    fun provideSavedWorkUseCase(
        getWorksUseCase: GetWorksUseCase,
        deleteWorksUseCase: DeleteWorksUseCase
    ): SavedWorkUseCase {
        return SavedWorkUseCase(
            getWorksUseCase = getWorksUseCase,
            deleteWorksUseCase = deleteWorksUseCase
        )
    }
}