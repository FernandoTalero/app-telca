package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase base para todos los tipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
abstract class Garment {
    abstract val type: GarmentType
    abstract val garmentSubTypes: List<GarmentSubType>
}