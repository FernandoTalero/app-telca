package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela una Falda de media capa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class FaldaMediaCapa : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.FALDA_MEDIA_CAPA
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_CINTURA,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.falda_media_capa_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoCintura = mv.getValueForType(MeasurementType.CONTORNO_CINTURA)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var bodyMultiFactor = 1
        val mediaCapaSupplement = contornoCintura / Math.PI
        val bodySupplement = 5

        val largoCuerpo = largoFalda + mediaCapaSupplement + bodySupplement

        // Si el contorno de cintura es mayor que el ancho de la tela
        if(contornoCintura > anchoTela) {
            // La medida determinante es el contorno de cintura.
            bodyMultiFactor = ceil(contornoCintura / anchoTela).toInt()
        }

        val largoTotal = (largoCuerpo * bodyMultiFactor) * 2 // Las medias capas son dos largos

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}