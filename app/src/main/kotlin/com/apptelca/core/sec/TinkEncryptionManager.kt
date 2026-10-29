package com.apptelca.core.sec

import android.content.Context
import androidx.core.content.edit
import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.security.KeyStore
import javax.inject.Inject

/**
 * Implementa funciones criptográficas utilizando la librería Google Tink.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class TinkEncryptionManager @Inject constructor(private val context: Context) : EncryptionManager {
    private lateinit var aead: Aead

    // Alias de la clave maestra
    private val masterKeyAlias = "keyset_master_key"

    // URI de la clave maestra para cifrar el Keyset almacenada en el Android KeyStore
    private val masterKeyUri = "android-keystore://$masterKeyAlias"

    // Nombre del Keyset
    private val keysetName = "secure_keyset"

    // Nombre del archivo ShraredPreferences donde se almacena el Keyset
    private val keysetSharedPref = "keyset_shared_preferences"

    init {
        // Registramos las configuraciones AEAD obligatorias para Tink
        AeadConfig.register()

        // Inicializamos Tink
        initTink()
    }

    /**
     * Inicializa el AEAD.
     */
    private fun initTink() {
        try {
            aead = createKeysetManager().keysetHandle.getPrimitive(
                RegistryConfiguration.get(),
                Aead::class.java
            )
        } catch(_: Exception) {
            /* Si se produce una excepción, reseteamos el Keyset e intentamos iniciar de
            * nuevo el AEAD */
            resetKeysetAndRetry()
        }
    }

    /**
     * Crea un Keyset cifrado y almacenado en ShraredPreferences.
     *
     * @return Un AndroidKeysetManager.
     */
    private fun createKeysetManager(): AndroidKeysetManager =
        AndroidKeysetManager.Builder()
            // Establecemos la clave maestra
            .withMasterKeyUri(masterKeyUri)
            // Establecemos la plantilla de la primitiva de encriptación
            .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
            // Indicamos dónde se almacena el Keyset
            .withSharedPref(context, keysetName, keysetSharedPref)
            // Construimos el AndroidKeysetManager
            .build()

    /**
     * Resetea el Keyset.
     */
    private fun resetKeysetAndRetry() {
        try {
            // Borramos el archivo de SharedPreferences donde se almacena el Keyset
            context.getSharedPreferences(keysetSharedPref, Context.MODE_PRIVATE)
                .edit { clear() }

            // Borramos la clave maestra del Android KeyStore si aún existe
            val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
                load(null)
            }

            if(keyStore.containsAlias(masterKeyAlias)) {
                keyStore.deleteEntry(masterKeyAlias)
            }

            // Intentamos de nuevo la creación del Keyset y la incialización del AEAD
            aead = createKeysetManager().keysetHandle.getPrimitive(
                RegistryConfiguration.get(),
                Aead::class.java
            )
        } catch(e: Exception) {
            throw IllegalStateException("Almacenamiento seguro no disponible", e)
        }
    }

    /**
     * Cifra un texto plano.
     *
     * @param plainText Texto a cifrar.
     * @return El texto cifrado en un formato de array de bytes.
     */
    override fun encrypt(plainText: String): ByteArray =
        aead.encrypt(plainText.toByteArray(Charsets.UTF_8), null)

    /**
     * Descrifra un texto cifrado.
     *
     * @param cipherText El texto cifrado en formato de array de bytes.
     * @return El texto descifrado.
     */
    override fun decrypt(cipherText: ByteArray): String =
        String(aead.decrypt(cipherText, null), Charsets.UTF_8)
}