package com.apptelca.domain.usecase.savedwork

import com.apptelca.domain.model.Work
import com.apptelca.domain.repository.WorkRepository

/**
 * Elimina una lista de trabajos guardados.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class DeleteWorksUseCase(private val workRepository: WorkRepository) {
    /**
     * Elimina una lista de trabajos guardados.
     *
     * @return El número de trabajos eliminados.
     */
    suspend operator fun invoke(worksToDelete: List<Work>): Int {
        return workRepository.deleteWorks(worksToDelete)
    }
}