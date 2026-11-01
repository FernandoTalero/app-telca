package com.apptelca.core.extension

/**
 * Convierte un ByteArray a una cadena de texto en formato
 * hexadecimal de SQLCipher.
 *
 * @return El ByteArray convertido en un string hexadecimal
 * de SQLCipher.
 */
fun ByteArray.toSQLCipherHex(): String {
    val hexString = joinToString("") { byte ->
        String.format("%02x", byte)
    }

    return "x'$hexString'"
}