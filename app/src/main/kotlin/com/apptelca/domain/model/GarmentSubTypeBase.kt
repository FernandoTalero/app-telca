package com.apptelca.domain.model

import com.apptelca.domain.model.garment.subgarment.SubGarment

/**
 * Define la base para los subtipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
interface GarmentSubTypeBase : TypeBase {
    override fun createObject(): SubGarment
}