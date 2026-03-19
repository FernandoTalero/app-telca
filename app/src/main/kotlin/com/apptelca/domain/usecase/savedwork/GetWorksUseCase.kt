package com.apptelca.domain.usecase.savedwork

import com.apptelca.domain.model.Work
import com.apptelca.domain.repository.WorkRepository
import kotlinx.coroutines.flow.Flow

/**
 * Obtiene de la base de datos un Flow con una lista de objetos Work.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class GetWorksUseCase(private val workRepository: WorkRepository) {
    /**
     * Obtiene de la base de datos un Flow con una lista de objetos Work.
     *
     * @return Un Flow con una lista de objetos Work.
     */
    operator fun invoke(): Flow<List<Work>> {
        return workRepository.getWorks()
    }
}