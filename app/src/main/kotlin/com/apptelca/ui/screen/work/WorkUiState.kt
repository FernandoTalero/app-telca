package com.apptelca.ui.screen.work

import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import com.apptelca.ui.component.SimpleTextFieldUiState

/**
 * Almacena el estado de la UI de la screen Work.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class WorkUiState(
    // Pantalla principal
    val workId: Long = 0L,
    val workName: String = "",
    val garmentType: GarmentType? = null,
    val garmentSubType: GarmentSubType? = null,
    val measurementsTextFields: Map<MeasurementType, MeasurementTextFieldUiState> = emptyMap(),

    // Diálogo para la confirmación de la limpieza de datos del trabajo actual
    val showCleanWorkDialog: Boolean = false,

    // Diálogo para la confirmación del cambio de tipo de prenda
    val showUpdateGarmentTypeDialog: Boolean = false,

    // Diálogo de resultado del cálculo
    val calculationResult: String = "",
    val showResultDialog: Boolean = false,

    // BottomSheet para guardar o actualizar un trabajo
    val showBottomSheet: Boolean = false,
    val bottomSheetWorkNameTextField: SimpleTextFieldUiState = SimpleTextFieldUiState()
)