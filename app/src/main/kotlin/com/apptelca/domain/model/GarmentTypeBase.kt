package com.apptelca.domain.model

import com.apptelca.domain.model.garment.Garment

/**
 * Define la base para los tipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface GarmentTypeBase : TypeBase {
    override fun createObject(): Garment
}