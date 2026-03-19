package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela una Chaqueta.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Chaqueta : Garment() {
    override val type: GarmentType = GarmentType.CHAQUETA
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.CHAQUETA_BASICA
    )
}