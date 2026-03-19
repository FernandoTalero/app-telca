package com.apptelca.domain.usecase.work

import com.apptelca.domain.model.Work
import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import com.apptelca.domain.repository.WorkRepository

/**
 * Actualiza un objeto Work junto con sus medidas en la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class UpdateWorkUseCase(private val workRepository: WorkRepository) {
    /**
     * Actualiza un objeto Work junto con sus medidas en la base de datos.
     *
     * @param work El objeto Work a actualizar.
     * @param measurements Una lista con objetos MeasurementValue para las medidas.
     */
    suspend operator fun invoke(work: Work, measurements: List<MeasurementValue>) {
        workRepository.updateWork(work, measurements)
    }
}