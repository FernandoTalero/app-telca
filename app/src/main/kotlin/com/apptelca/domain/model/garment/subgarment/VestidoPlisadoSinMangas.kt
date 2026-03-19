package com.apptelca.domain.model.garment.subgarment

import com.apptelca.R
import com.apptelca.core.ResourceProvider
import kotlin.math.ceil

/**
 * Clase que modela un Vestido plisado sin mangas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class VestidoPlisadoSinMangas : SubGarment() {
    override val subType: GarmentSubType = GarmentSubType.VESTIDO_PLISADO_SIN_MANGAS
    override val measurements: List<MeasurementType> = listOf(
        MeasurementType.ANCHO_TELA,
        MeasurementType.CONTORNO_PECHO,
        MeasurementType.CONTORNO_CADERA,
        MeasurementType.LARGO_TALLE,
        MeasurementType.LARGO_FALDA
    )

    override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): String {
        val resultString = rp.getString(R.string.vestido_plisado_sin_mangas_result)
        val anchoTela = mv.getValueForType(MeasurementType.ANCHO_TELA)
        val contornoPecho = mv.getValueForType(MeasurementType.CONTORNO_PECHO)
        val contornoCadera = mv.getValueForType(MeasurementType.CONTORNO_CADERA) * 3 // Falda plisada
        val largoTalle = mv.getValueForType(MeasurementType.LARGO_TALLE)
        val largoFalda = mv.getValueForType(MeasurementType.LARGO_FALDA)

        var upperBodyMultiFactor = 1
        var lowerBodyMutilFactor = 1
        val lowerBodySupplement = 10

        val largoInferior = largoFalda + lowerBodySupplement

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

        val largoTotal = (largoTalle * upperBodyMultiFactor) +
                (largoInferior * lowerBodyMutilFactor)

        val formattedString = String.format(resultString, anchoTela, largoTotal)
        return formattedString
    }
}