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
    private val encryptionManager: EncryptionManager,
    context: Context
) {
    private val keyFile = File(context.filesDir, "sec_db_key.enc")

    /**
     * Obtiene o crea la clave de cifrado de la base de datos.
     *
     * @return La clave de cifrado de la base de datos en formato de array de bytes.
     */
    fun getOrCreatePassphrase(): ByteArray {
        return if(keyFile.exists()) {
            encryptionManager.decrypt(keyFile.readBytes())
        } else {
            val plainPassphrase = ByteArray(32)
            SecureRandom().nextBytes(plainPassphrase)

            keyFile.writeBytes(encryptionManager.encrypt(plainPassphrase))
            plainPassphrase
        }
    }
}