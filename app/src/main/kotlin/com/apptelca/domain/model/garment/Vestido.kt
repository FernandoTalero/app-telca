package com.apptelca.domain.model.garment

import com.apptelca.domain.model.garment.subgarment.GarmentSubType

/**
 * Clase que modela un Vestido.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class Vestido : Garment() {
    override val type: GarmentType = GarmentType.VESTIDO
    override val garmentSubTypes: List<GarmentSubType> = listOf(
        GarmentSubType.VESTIDO_RECTO,
        GarmentSubType.VESTIDO_RECTO_SIN_MANGAS,
        GarmentSubType.VESTIDO_CAPA_ENTERA,
        GarmentSubType.VESTIDO_CAPA_ENTERA_SIN_MANGAS,
        GarmentSubType.VESTIDO_MEDIA_CAPA,
        GarmentSubType.VESTIDO_MEDIA_CAPA_SIN_MANGAS,
        GarmentSubType.VESTIDO_PLISADO,
        GarmentSubType.VESTIDO_PLISADO_SIN_MANGAS
    )
}