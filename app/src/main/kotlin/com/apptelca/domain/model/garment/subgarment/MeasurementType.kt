package com.apptelca.domain.model.garment.subgarment

/**
 * Enumeración con los distintos tipos de medidas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
enum class MeasurementType(c: Int) {
    ANCHO_TELA(1),
    CONTORNO_PECHO(2),
    CONTORNO_CINTURA(3),
    CONTORNO_CADERA(4),
    CONTORNO_MANGA(5),
    LARGO_TALLE(6),
    LARGO_MANGA(7),
    LARGO_CAMISA(8),
    LARGO_FALDA(9),
    LARGO_PANTALON(10),
    LARGO_CHAQUETA(11),
    LARGO_ABRIGO(12);

    // Propiedad que define un código interno para cada constante
    val code: Int = c
}