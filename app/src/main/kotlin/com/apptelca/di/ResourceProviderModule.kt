package com.apptelca.di

import android.content.Context
import com.apptelca.core.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la inyección de dependencias de objetos ResourceProvider.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Module
@InstallIn(SingletonComponent::class)
object ResourceProviderModule {
    @Singleton
    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider(context)
    }
}