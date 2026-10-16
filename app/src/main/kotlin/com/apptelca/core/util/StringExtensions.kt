package com.apptelca.core.util

/**
 * Convierte una cadena de texto en un Double.
 *
 * @return El valor Double resultado de la conversión o 0.0 en caso de
 * que ésta no sea posible.
 */
fun String.stringToDouble(): Double {
    return this.trim().replace(",", ".")
        .toDoubleOrNull() ?: 0.0
}