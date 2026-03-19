package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela un Vestido de media capa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class VestidoMediaCapa : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.VESTIDO_MEDIA_CAPA
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_PECHO,
        MeasurementType.CONTORNO_CINTURA,
        MeasurementType.CONTORNO_MANGA,
        MeasurementType.LARGO_TALLE,
        MeasurementType.LARGO_MANGA,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.vestido_media_capa_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoPecho = mv.getValueForType(MeasurementType.CONTORNO_PECHO)
        val contornoCintura = mv.getValueForType(MeasurementType.CONTORNO_CINTURA)
        val contornoManga = mv.getValueForType(MeasurementType.CONTORNO_MANGA)
        val largoTalle = mv.getValueForType(MeasurementType.LARGO_TALLE)
        var largoManga = mv.getValueForType(MeasurementType.LARGO_MANGA)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var upperBodyMultiFactor = 1
        val mediaCapaSupplement = contornoCintura / Math.PI
        var sleeveMultiFactor = 1
        val lowerBodySupplement = 10
        val sleeveSupplement = 5

        val largoInferior = largoFalda + mediaCapaSupplement + lowerBodySupplement
        largoManga += sleeveSupplement

        // Si el contorno de pecho es mayor que el ancho de la tela
        if(contornoPecho > anchoTela) {
            // La medida determinante es el contorno de pecho
            upperBodyMultiFactor = ceil(contornoPecho / anchoTela).toInt()
        }

        val sleeveTotal = contornoManga * 2
        // Si el contorno de las dos mangas son mayores que el ancho de la tela
        if(sleeveTotal > anchoTela) {
            sleeveMultiFactor = ceil(sleeveTotal / anchoTela).toInt()
        }

        val largoTotal = (largoTalle * upperBodyMultiFactor) +
                (largoInferior * 2) + // Las medias capas son dos largos
                (largoManga * sleeveMultiFactor)

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}