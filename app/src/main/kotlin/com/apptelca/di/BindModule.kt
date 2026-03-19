package com.apptelca.di

import com.apptelca.domain.repository.NoteRepository
import com.apptelca.data.repository.NoteRepositoryImpl
import com.apptelca.domain.repository.WorkRepository
import com.apptelca.data.repository.WorkRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de vinculación de Hilt para la inyección de dependencias.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    @Singleton
    abstract fun bindWorkRepository(workRepositoryImpl: WorkRepositoryImpl): WorkRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(noteRepositoryIml: NoteRepositoryImpl): NoteRepository
}