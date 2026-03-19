package com.apptelca.domain.usecase

import com.apptelca.domain.usecase.savedwork.DeleteWorksUseCase
import com.apptelca.domain.usecase.savedwork.GetWorksUseCase

/**
 * Envuelve todos las funciones de casos de uso para los trabajos guardados.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class SavedWorkUseCase(
    val getWorksUseCase: GetWorksUseCase,
    val deleteWorksUseCase: DeleteWorksUseCase
)