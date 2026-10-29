package com.apptelca.core.sec

/**
 * Ofrece funciones criptográficas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface EncryptionManager {
    /**
     * Cifra un texto plano.
     *
     * @param plainText Texto a cifrar.
     * @return El texto cifrado en un formato de array de bytes.
     */
    fun encrypt(plainText: String): ByteArray

    /**
     * Descrifra un texto cifrado.
     *
     * @param cipherText El texto cifrado en formato de array de bytes.
     * @return El texto descifrado.
     */
    fun decrypt(cipherText: ByteArray): String
}