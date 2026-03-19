package com.apptelca.domain.model.garment

import com.apptelca.domain.model.GarmentTypeBase

/**
 * Enumeración con los distintos tipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
enum class GarmentType(c: Int) : GarmentTypeBase {
    CAMISA(1),
    FALDA(2),
    PANTALON(3),
    VESTIDO(4),
    CHALECO(5),
    CHAQUETA(6),
    ABRIGO(7);

    // Propiedad que define un código interno para cada constante
    val code: Int = c

    /**
     * Crea un objeto correspondiente para cada constante de la enumeración.
     *
     * @return Devuelve un objeto Garment.
     */
    override fun createObject(): Garment {
        return when(this) {
            CAMISA -> Camisa()
            FALDA -> Falda()
            PANTALON -> Pantalon()
            VESTIDO -> Vestido()
            CHALECO -> Chaleco()
            CHAQUETA -> Chaqueta()
            ABRIGO -> Abrigo()
        }
    }
}