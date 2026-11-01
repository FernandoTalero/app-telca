package com.apptelca.data.local.database

import android.content.Context
import com.apptelca.core.sec.EncryptionManager
import java.io.File
import java.security.SecureRandom
import javax.inject.Inject

/**
 * Gestiona la clave de cifrado de la base de datos.
 *
 * @author Fernando Talero
 * @since 1.0
 * @version 1.0
 */
class DbPassphraseHelper @Inject constructor(
    context: Context,
    private val encryptionManager: EncryptionManager
) {
    private val keyFile = File(context.filesDir, "sec_db_key.enc")

    /**
     * Obtiene o crea la clave de cifrado de la base de datos.
     *
     * @return La clave de cifrado de la base de datos en formato de array de bytes.
     */
    fun getOrCreatePassphrase(): ByteArray {
        // Si existe el archivo con la clave encriptada...
        return if(keyFile.exists()) {
            // ... lo leemos y la retornamos
            encryptionManager.decrypt(keyFile.readBytes())
        } else {
            // En caso contrario, creamos una clave de 256 bits
            val passphrase = ByteArray(32)
            SecureRandom().nextBytes(passphrase)

            // Guardamos encriptada la clave y la retornamos
            keyFile.writeBytes(encryptionManager.encrypt(passphrase))
            passphrase
        }
    }
}