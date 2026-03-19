package com.apptelca.domain.usecase.work

import com.apptelca.domain.model.Work
import com.apptelca.domain.repository.WorkRepository

/**
 * Obtiene un objeto Work de la base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class GetWorkUseCase(private val workRepository: WorkRepository) {
    /**
     * Obtiene un objeto Work de la base de datos.
     *
     * @param workId El id del objeto Work que se desea obtener.
     * @return El objeto Work cuyo id se pasa como argumento.
     */
    suspend operator fun invoke(workId: Long): Work? {
        return workRepository.getWork(workId)
    }
}