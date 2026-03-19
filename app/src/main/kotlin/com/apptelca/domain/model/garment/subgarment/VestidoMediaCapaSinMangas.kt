package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela un Vestido de media capa sin mangas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class VestidoMediaCapaSinMangas : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.VESTIDO_MEDIA_CAPA_SIN_MANGAS
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_PECHO,
        MeasurementType.CONTORNO_CINTURA,
        MeasurementType.LARGO_TALLE,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.vestido_media_capa_sin_mangas_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoPecho = mv.getValueForType(MeasurementType.CONTORNO_PECHO)
        val contornoCintura = mv.getValueForType(MeasurementType.CONTORNO_CINTURA)
        val largoTalle = mv.getValueForType(MeasurementType.LARGO_TALLE)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var upperBodyMultiFactor = 1
        val mediaCapaSupplement = contornoCintura / Math.PI
        val lowerBodySupplement = 10

        val largoInferior = largoFalda + mediaCapaSupplement + lowerBodySupplement

        // Si el contorno de pecho es mayor que el ancho de la tela
        if(contornoPecho > anchoTela) {
            // La medida determinante es el contorno de pecho
            upperBodyMultiFactor = ceil(contornoPecho / anchoTela).toInt()
        }

        val largoTotal = (largoTalle * upperBodyMultiFactor) +
                (largoInferior * 2) // Las medias capas son dos largos

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}