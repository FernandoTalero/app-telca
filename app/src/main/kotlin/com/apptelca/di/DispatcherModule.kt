package com.apptelca.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Calificador para el Dispatcher Default.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

/**
 * Calificador para el Dispatcher IO.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/**
 * Módulo de Hilt para la inyección de dependencias de los dispatchers de coroutines.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}