package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela un Vestido plisado.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class VestidoPlisado : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.VESTIDO_PLISADO
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_PECHO,
        MeasurementType.CONTORNO_CADERA,
        MeasurementType.CONTORNO_MANGA,
        MeasurementType.LARGO_TALLE,
        MeasurementType.LARGO_MANGA,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.vestido_plisado_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoPecho = mv.getValueForType(MeasurementType.CONTORNO_PECHO)
        val contornoCadera = mv.getValueForType(MeasurementType.CONTORNO_CADERA) * 3 // Falda plisada
        val contornoManga = mv.getValueForType(MeasurementType.CONTORNO_MANGA)
        val largoTalle = mv.getValueForType(MeasurementType.LARGO_TALLE)
        var largoManga = mv.getValueForType(MeasurementType.LARGO_MANGA)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var upperBodyMultiFactor = 1
        var lowerBodyMutilFactor = 1
        var sleeveMultiFactor = 1
        val lowerBodySupplement = 10
        val sleeveSupplement = 5

        val largoInferior = largoFalda + lowerBodySupplement
        largoManga += sleeveSupplement

        // Si el contorno de pecho o de cadera son mayores que el ancho de la tela
        if(contornoPecho > anchoTela || contornoCadera > anchoTela) {
            /* Si el contorno de pecho es mayor o igual que el de cadera, la medida
            * determinante es el contorno de pecho. */
            if(contornoPecho >= contornoCadera) {
                upperBodyMultiFactor = ceil(contornoPecho / anchoTela).toInt()
            } else {
                // En caso contrario, la medida determinante es el contorno de cadera
                lowerBodyMutilFactor = ceil(contornoCadera / anchoTela).toInt()
            }
        }

        val sleeveTotal = contornoManga * 2
        // Si el contorno de las dos mangas son mayores que el ancho de la tela
        if(sleeveTotal > anchoTela) {
            sleeveMultiFactor = ceil(sleeveTotal / anchoTela).toInt()
        }

        val largoTotal = (largoTalle * upperBodyMultiFactor) +
                (largoInferior * lowerBodyMutilFactor) +
                (largoManga * sleeveMultiFactor)

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}