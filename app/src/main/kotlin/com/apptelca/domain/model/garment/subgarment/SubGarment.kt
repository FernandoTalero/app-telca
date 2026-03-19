package com.apptelca.domain.model.garment.subgarment

import com.apptelca.domain.model.Calculator
import com.apptelca.core.ResourceProvider

/**
 * Clase base para todos los subtipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
abstract class SubGarment : Calculator {
    abstract val subType: GarmentSubType
    abstract val measurements: List<MeasurementType>
    abstract override fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): CharSequence

    /**
     * Función de extensión para obtener el valor de un objeto MeasurementValue dentro
     * de una lista de tipo MeasurementValue.
     *
     * @param measurementType El MeasurementType del objeto MeasurementValue.
     * @return El valor Double del objeto MeasurementValue.
     */
    fun List<MeasurementValue>.getValueForType(measurementType: MeasurementType): Double {
        return find { it.measurementType == measurementType }?.value ?: 0.0
    }
}