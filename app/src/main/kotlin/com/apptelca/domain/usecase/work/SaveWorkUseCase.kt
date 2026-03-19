package com.apptelca.domain.usecase.work

import com.apptelca.domain.model.Work
import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import com.apptelca.domain.repository.WorkRepository

/**
 * Almacena un objeto Work junto con sus medidas en la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class SaveWorkUseCase(private val workRepository: WorkRepository) {
    /**
     * Almacena un objeto Work junto con sus medidas en la base de datos.
     *
     * @param work El objeto Work a almacenar.
     * @param measurements Una lista con objetos MeasurementValue para las medidas.
     * @return El ID del objeto Work almacenado.
     */
    suspend operator fun invoke(work: Work, measurements: List<MeasurementValue>): Long {
        return workRepository.insertWork(work, measurements)
    }
}