package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela una Camisa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Camisa : Garment() {
    override val type: GarmentType = GarmentType.CAMISA
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.CAMISA_BASICA
    )
}