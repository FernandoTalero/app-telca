package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela una Falda de capa entera.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class FaldaCapaEntera : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.FALDA_CAPA_ENTERA
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_CINTURA,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.falda_capa_entera_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoCintura = mv.getValueForType(MeasurementType.CONTORNO_CINTURA)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var bodyMultiFactor = 1
        val capaEnteraSupplement = contornoCintura / 6.28
        val bodySupplement = 5

        val largoCuerpo = largoFalda + capaEnteraSupplement + bodySupplement

        // Si el contorno de cintura es mayor que el ancho de la tela
        if(contornoCintura > anchoTela) {
            // La medida determinante es el contorno de cintura.
            bodyMultiFactor = ceil(contornoCintura / anchoTela).toInt()
        }

        val largoTotal = (largoCuerpo * bodyMultiFactor) * 4 // Las capas enteras son cuatro largos

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}