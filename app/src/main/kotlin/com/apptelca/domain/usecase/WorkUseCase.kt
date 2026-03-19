package com.apptelca.domain.usecase

import com.apptelca.domain.usecase.work.GetWorkUseCase
import com.apptelca.domain.usecase.work.GetWorkWithMeasurementsUseCase
import com.apptelca.domain.usecase.work.SaveWorkUseCase
import com.apptelca.domain.usecase.work.UpdateWorkUseCase
import com.apptelca.domain.usecase.work.ValidateMeasurementUseCase
import com.apptelca.domain.usecase.work.ValidateMeasurementValueUseCase
import com.apptelca.domain.usecase.work.ValidateWorkNameUseCase
import com.apptelca.domain.usecase.work.ValidateWorkNameValueUseCase

/**
 * Envuelve todos las funciones de casos de uso para los trabajos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class WorkUseCase(
    val getWorkWithMeasurementsUseCase: GetWorkWithMeasurementsUseCase,
    val getWorkUseCase: GetWorkUseCase,
    val saveWorkUseCase: SaveWorkUseCase,
    val updateWorkUseCase: UpdateWorkUseCase,
    val validateMeasurementValueUseCase: ValidateMeasurementValueUseCase,
    val validateMeasurementUseCase: ValidateMeasurementUseCase,
    val validateWorkNameValueUseCase: ValidateWorkNameValueUseCase,
    val validateWorkNameUseCase: ValidateWorkNameUseCase
)