package com.apptelca.data.local.database

import android.content.Context
import com.apptelca.core.sec.EncryptionManager
import java.io.File
import java.security.SecureRandom

/**
 * Gestiona la seguridad y cifrado de la base de datos.
 *
 * @author Fernando Talero
 * @since 1.0
 * @version 1.0
 */
class SecureDBHelper(
    private val context: Context,
    private val encryptionManager: EncryptionManager
) {
    private val keyFile = File(context.filesDir, "sec_db_key.enc")

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