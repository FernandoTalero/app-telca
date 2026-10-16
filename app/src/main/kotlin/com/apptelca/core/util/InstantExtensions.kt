package com.apptelca.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Da formato de fecha y hora a un objeto de tipo Instant.
 *
 * @return Una cadena de texto con la fecha formateada.
 */
fun Instant.formatDateTime(): String {
    return DateTimeFormatter
        .ofPattern("dd/MM/yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(this)
}