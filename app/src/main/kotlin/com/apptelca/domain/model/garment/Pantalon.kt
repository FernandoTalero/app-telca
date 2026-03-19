package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela un Pantalon.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Pantalon : Garment() {
    override val type: GarmentType = GarmentType.PANTALON
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.PANTALON_BASICO
    )
}