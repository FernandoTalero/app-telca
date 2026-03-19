package com.apptelca.domain.model

/**
 * Define la base para los tipos y subtipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface TypeBase {
    /**
     * Crea un objeto para cada uno de los tipos o subtipos de prendas
     * de ropa.
     */
    fun createObject(): Any
}