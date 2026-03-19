package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela un Chaleco.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Chaleco : Garment() {
    override val type: GarmentType = GarmentType.CHALECO
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.CHALECO_BASICO
    )
}