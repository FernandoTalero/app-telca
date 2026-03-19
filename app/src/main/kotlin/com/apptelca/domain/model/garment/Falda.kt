package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela una Falda.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Falda : Garment() {
    override val type: GarmentType = GarmentType.FALDA
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.FALDA_RECTA,
        GarmentSubType.FALDA_CAPA_ENTERA,
        GarmentSubType.FALDA_MEDIA_CAPA,
        GarmentSubType.FALDA_PLISADA
    )
}