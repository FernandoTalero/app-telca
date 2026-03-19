package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela un Chaleco básico.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class ChalecoBasico : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.CHALECO_BASICO
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_PECHO,
        MeasurementType.CONTORNO_CINTURA,
        MeasurementType.LARGO_TALLE
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.chaleco_basico_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoPecho = mv.getValueForType(MeasurementType.CONTORNO_PECHO)
        val contornoCintura = mv.getValueForType(MeasurementType.CONTORNO_CINTURA)
        val largoTalle = mv.getValueForType(MeasurementType.LARGO_TALLE)

        var bodyMultiFactor = 1
        val bodySupplement = 5

        val largoCuerpo = largoTalle + bodySupplement

        // Si el contorno de pecho o de cintura son mayores que el ancho de la tela
        if(contornoPecho > anchoTela || contornoCintura > anchoTela) {
            /* Si el contorno de pecho es mayor o igual que el de cintura, la medida
            * determinante es el contorno de pecho. */
            bodyMultiFactor = if(contornoPecho >= contornoCintura) {
                ceil(contornoPecho / anchoTela).toInt()
            } else {
                // En caso contrario, la medida determinante es el contorno de cintura
                ceil(contornoCintura / anchoTela).toInt()
            }
        }

        val largoTotal = (largoCuerpo * bodyMultiFactor)

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}