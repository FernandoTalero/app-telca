package com.apptelca.domain.usecase.work

import com.apptelca.domain.model.WorkWithMeasurements
import com.apptelca.domain.repository.WorkRepository

/**
 * Obtiene de la base de datos un trabajo junto con sus medidas.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class GetWorkWithMeasurementsUseCase(private val workRepository: WorkRepository) {
    /**
     * Obtiene de la base de datos un trabajo junto con sus medidas.
     *
     * @param workId El ID del trabajo que se desea obtener.
     * @return El trabajo con sus medidas cuyo ID se pasa como argumento.
     */
    suspend operator fun invoke(workId: Long): WorkWithMeasurements? {
        return workRepository.getWorkWithMeasurements(workId)
    }
}