package com.apptelca

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application.
 *
 * @author Fernando Talero
 * @version 1.1
 * @since 1.0
 */
@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("sqlcipher")
    }
}