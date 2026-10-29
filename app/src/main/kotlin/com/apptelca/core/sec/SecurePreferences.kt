package com.apptelca.core.sec

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.apptelca.core.extension.dataStore

/**
 * Gestiona el almacenamiento seguro de cadenas de texto.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class SecurePreferences(private val context: Context) {
    private val encryptionManager = TinkEncryptionManager(context)

    /**
     * Almacena una cadena de texto de forma segura.
     *
     * @param key La clave asociada a la cadena de texto.
     * @param plainText La cadena de texto a almacenar.
     */
    suspend fun saveToken(key: String, plainText: String) {
        // Creamos la Preferences Key
        val tokenKey = stringPreferencesKey(key)

        // Encriptamos la cadena de texto
        val encryptedString = Base64.encodeToString(
            encryptionManager.encrypt(plainText), Base64.NO_WRAP
        )

        context.dataStore.edit { preferences ->
            preferences[tokenKey] = encryptedString
        }
    }
}