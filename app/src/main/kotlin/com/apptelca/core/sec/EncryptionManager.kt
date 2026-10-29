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
     * @param plainText Texto a cifrar en formato de array de bytes.
     * @return El texto cifrado en formato de array de bytes.
     */
    fun encrypt(plainText: ByteArray): ByteArray

    /**
     * Descrifra un texto cifrado.
     *
     * @param cipherText El texto cifrado en formato de array de bytes.
     * @return El texto descifrado en formato de array de bytes.
     */
    fun decrypt(cipherText: ByteArray): ByteArray
}