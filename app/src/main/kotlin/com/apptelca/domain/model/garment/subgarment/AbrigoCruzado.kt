package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela un Abrigo cruzado.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class AbrigoCruzado : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.ABRIGO_CRUZADO
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_PECHO,
        MeasurementType.CONTORNO_CADERA,
        MeasurementType.CONTORNO_MANGA,
        MeasurementType.LARGO_MANGA,
        MeasurementType.LARGO_ABRIGO
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): CharSequence {
        val resultString = rp.getString(R.string.abrigo_cruzado_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        var contornoPecho = mv.getValueForType(MeasurementType.CONTORNO_PECHO)
        var contornoCadera = mv.getValueForType(MeasurementType.CONTORNO_CADERA)
        val contornoManga = mv.getValueForType(MeasurementType.CONTORNO_MANGA)
        var largoManga = mv.getValueForType(MeasurementType.LARGO_MANGA)
        val largoAbrigo = mv.getValueForType(MeasurementType.LARGO_ABRIGO)

        var bodyMultiFactor = 1
        var sleeveMultiFactor = 1
        val bodySupplement = 10
        val sleeveSupplement = 5

        val largoCuerpo = largoAbrigo + bodySupplement
        largoManga += sleeveSupplement

        // Si el contorno de pecho o de cadera son mayores que el ancho de la tela
        if(contornoPecho > anchoTela || contornoCadera > anchoTela) {
            /* Si el contorno de pecho es mayor o igual que el de cadera, la medida
            * determinante es el contorno de pecho. */
            bodyMultiFactor = if(contornoPecho >= contornoCadera) {
                contornoPecho += contornoPecho / 8
                ceil(contornoPecho / anchoTela).toInt()
            } else {
                // En caso contrario, la medida determinante es el contorno de cadera
                contornoCadera += contornoCadera / 8
                ceil(contornoCadera / anchoTela).toInt()
            }
        }

        val sleeveTotal = contornoManga * 2
        // Si el contorno de las dos mangas son mayores que el ancho de la tela
        if(sleeveTotal > anchoTela) {
            sleeveMultiFactor = ceil(sleeveTotal / anchoTela).toInt()
        }

        val largoTotal = (largoCuerpo * bodyMultiFactor) + (largoManga * sleeveMultiFactor)

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}