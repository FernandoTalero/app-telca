package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela una Falda recta.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class FaldaRecta : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.FALDA_RECTA
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_CINTURA,
        MeasurementType.CONTORNO_CADERA,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.falda_recta_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoCintura = mv.getValueForType(MeasurementType.CONTORNO_CINTURA)
        val contornoCadera = mv.getValueForType(MeasurementType.CONTORNO_CADERA)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var bodyMultiFactor = 1
        val bodySupplement = 10

        val largoCuerpo = largoFalda + bodySupplement

        // Si el contorno de cintura o de cadera son mayores que el ancho de la tela
        if(contornoCintura > anchoTela || contornoCadera > anchoTela) {
            /* Si el contorno de cintura es mayor o igual que el de cadera, la medida
            * determinante es el contorno de cintura. */
            bodyMultiFactor = if(contornoCintura >= contornoCadera) {
                ceil(contornoCintura / anchoTela).toInt()
            } else {
                // En caso contrario, la medida determinante es el contorno de cadera
                ceil(contornoCadera / anchoTela).toInt()
            }
        }

        val largoTotal = (largoCuerpo * bodyMultiFactor)

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}