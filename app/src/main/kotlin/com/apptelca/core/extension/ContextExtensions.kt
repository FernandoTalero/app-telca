package com.apptelca.core.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Define un DataStore en la clase Context.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")