package com.apptelca.domain.model

import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import com.apptelca.core.ResourceProvider

/**
 * Define la función para el cálculo de la cantidad de tela necesaria
 * para cada subtipo de prenda de ropa.
 *
 * @author Fernando Talero
 * @version 1.2
 * @since 1.0
 */
interface Calculator {
    /**
     * Función para la implementacion del cálculo de la cantidad de
     * tela necesaria para un trabajo.
     */
    fun calculate(rp: ResourceProvider, mv: List<MeasurementValue>): CharSequence
}