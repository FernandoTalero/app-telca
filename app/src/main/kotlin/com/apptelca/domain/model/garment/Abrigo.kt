package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela un Abrigo.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Abrigo : Garment() {
    override val type: GarmentType = GarmentType.ABRIGO
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.ABRIGO_RECTO,
        GarmentSubType.ABRIGO_CRUZADO
    )
}