package com.apptelca.ui.screen.work

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.apptelca.R
import com.apptelca.core.ResourceProvider
import com.apptelca.core.Util
import com.apptelca.di.IoDispatcher
import com.apptelca.domain.model.Work
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import com.apptelca.domain.model.garment.subgarment.MeasurementValue
import com.apptelca.domain.usecase.WorkUseCase
import com.apptelca.ui.event.UiEvent
import com.apptelca.ui.navigation.NavRoutes
import com.apptelca.validator.CompositeValidator
import com.apptelca.validator.GreaterThanZeroValidator
import com.apptelca.validator.MeasurementValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

/**
 * Conecta la capa de dominio con la UI de la screen Work.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@HiltViewModel
class WorkViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val resourceProvider: ResourceProvider,
    private val workUseCase: WorkUseCase,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    // StateFlow para conservar el estado de la UI
    private val _uiState = MutableStateFlow(WorkUiState())
    val uiState: StateFlow<WorkUiState> = _uiState.asStateFlow()

    // SharedFlow para notificar los eventos de la UI
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    // Inicializamos el id del trabajo a partir de la ruta de navegación
    private val workId = savedStateHandle.toRoute<NavRoutes.Work>().workId

    init {
        // Obtenemos los datos del trabajo a partir de su id
        fetchWork()
    }

    /**
     * Muestra el diálogo para la confirmación de la limpieza de los datos
     * del trabajo actual.
     */
    fun showCleanWorkDialog() {
        _uiState.update {
            it.copy(showCleanWorkDialog = true)
        }
    }

    /**
     * OCulta el diálogo para la confirmación de la limpieza de los datos
     * del trabajo actual.
     */
    fun dismissCleanWorkDialog() {
        _uiState.update {
            it.copy(showCleanWorkDialog = false)
        }
    }

    /**
     * Elimina los datos del trabajo actual.
     */
    fun cleanWork() {
        _uiState.value = WorkUiState()

        // Indicamos en el WorkSession que no hay un trabajo cargado
        WorkSession.activeWorkId = null
    }

    /**
     * Muestra el diálogo de confirmación para el cambio de tipo
     * de prenda de ropa.
     */
    fun showUpdateGarmentTypeDialog() {
        _uiState.update {
            it.copy(showUpdateGarmentTypeDialog = true)
        }
    }

    /**
     * Oculta el diálogo de confirmación para el cambio de tipo
     * de prenda de ropa.
     */
    fun dismissUpdateGarmentTypeDialog() {
        _uiState.update {
            it.copy(showUpdateGarmentTypeDialog = false)
        }
    }

    /**
     * Actualiza el tipo de prenda de ropa.
     *
     * @param garmentType El tipo de prenda de ropa a actualizar.
     */
    fun updateGarmentType(garmentType: GarmentType) {
        /* Al actualizar el tipo de prenda, se inicia un nuevo trabajo, por lo
        * que es necesario establecer el workId a 0 y workName a una cadena de
        * texto vacía. */
        _uiState.update {
            it.copy(
                workId = 0,
                workName = "",
                garmentType = garmentType,
                garmentSubType = null,
                measurementsTextFields = emptyMap()
            )
        }
    }

    /**
     * Actualiza el subtipo de prenda de ropa.
     *
     * @param garmentSubType El subtipo de prenda de ropa a actualizar.
     */
    fun updateGarmentSubType(garmentSubType: GarmentSubType) {
        _uiState.update { it.copy(garmentSubType = garmentSubType) }

        // Actualizamos las medidas
        updateMeasurements()
    }

    /**
     * Actualiza la lista de medidas.
     */
    fun updateMeasurements() {
        // Obtenemos la lista de MeasurementType del GarmentSubtype
        val measurementTypes =
            uiState.value.garmentSubType?.createObject()?.measurements ?: emptyList()

        // Creamos un nuevo Map para las medidas
        val map = measurementTypes.associateWith {
            val measurementName = resourceProvider.getMeasurementTypeString(it)
            MeasurementTextFieldUiState(
                measurementType = it,
                label = measurementName,
                placeholder = measurementName
            )
        }

        // Actualizamos el mapa de las medidas del estado de la UI
        _uiState.update { it.copy(measurementsTextFields = map) }
    }

    /**
     * Actualiza el valor de una medida en el campo de texto correspondiente.
     *
     * @param measurementType El tipo de medida a actualizar.
     * @param value El valor de la medida a actualizar.
     */
    fun updateMeasurement(measurementType: MeasurementType, value: String) {
        if(workUseCase.validateMeasurementValueUseCase(value)) {
            _uiState.update { currentWorkState ->
                val updatedMeasurements = currentWorkState.measurementsTextFields.toMutableMap()
                updatedMeasurements[measurementType]?.let {
                    updatedMeasurements[measurementType] = it.copy(value = value)
                }

                currentWorkState.copy(measurementsTextFields = updatedMeasurements)
            }
        }
    }

    /**
     * Cierra el Dialog con el resultado del cálculo.
     */
    fun dismissResultDialog() {
        _uiState.update { it.copy(showResultDialog = false) }
    }

    /**
     * Muestra el BottomSheet para guardar o actualizar un trabajo.
     */
    fun showBottomSheet() {
        _uiState.update {
            val workNameTextField =
                it.bottomSheetWorkNameTextField.copy(value = uiState.value.workName)
            it.copy(showBottomSheet = true, bottomSheetWorkNameTextField = workNameTextField)
        }
    }

    /**
     * Cierra el BottomSheet para guardar o actualizar un trabajo.
     */
    fun dismissBottomSheet() {
        _uiState.update {
            val workNameTextField = it.bottomSheetWorkNameTextField.copy(
                value = "",
                isError = false,
                error = null
            )
            it.copy(showBottomSheet = false, bottomSheetWorkNameTextField = workNameTextField)
        }
    }

    /**
     * Actualiza el valor del campo de texto para el nombre de un trabajo.
     *
     * @param value El valor del campo de texto del nombre del trabajo.
     */
    fun updateBottomSheetTextField(value: String) {
        if(workUseCase.validateWorkNameValueUseCase(value)) {
            _uiState.update {
                val workNameTextField =
                    it.bottomSheetWorkNameTextField.copy(value = value)
                it.copy(bottomSheetWorkNameTextField = workNameTextField)
            }
        }
    }

    /**
     * Guarda o actualiza un trabajo.
     *
     * @param onSuccess Acción que se lleva a cabo al guardar un trabajo con éxito.
     * @param onError Acción que se lleva a cabo en caso de error.
     * @param onMeasurementsError Acción que se lleva a cabo en caso de error con las medidas.
     */
    fun bottomSheetSaveButtonClick(
        onSuccess: () -> Unit,
        onError: () -> Unit,
        onMeasurementsError: () -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            with(uiState.value) {
                val workId = workId
                val workName = bottomSheetWorkNameTextField.value.trim()
                val workNameIsError: Boolean
                val workNameError: String?
                val garmentType = garmentType
                val garmentSubType = garmentSubType
                /* Si ha habido errores (retorno null) en la validación de las medidas al
                * obtenerlas, mostramos los errores, cerramos el BottomSheet y salimos de
                * la función. */
                val measurements = getMeasurementsValues()
                if(measurements == null) {
                    onMeasurementsError(); return@with
                }

                // Si el nombre del trabajo es válido...
                if(workUseCase.validateWorkNameUseCase(workName)) {
                    // Inicializamos las variables de error para que no se muestre ninguno
                    workNameIsError = false
                    workNameError = null

                    // Guardamos o actualizamos el trabajo según si hay un id asociado
                    if(workId == 0L) {
                        saveWork(
                            Work(
                                name = workName,
                                type = garmentType!!,
                                subtype = garmentSubType!!,
                                dateTime = Instant.now()
                            ), measurements
                        )
                    } else {
                        getWork(workId)?.copy(name = workName, subtype = garmentSubType!!)?.let {
                            updateWork(it, measurements)
                        }
                    }

                    onSuccess()
                } else {
                    /* ... En caso contrario, inicialiamos las variables de error para mostrar
                    el error correspondiente */
                    workNameIsError = true
                    workNameError = resourceProvider.getString(R.string.no_valid_work_name)
                    onError()
                }

                // Indicamos al campo de texto del trabajo si debe mostrar un error o no
                val updatedTextField = bottomSheetWorkNameTextField.copy(
                    isError = workNameIsError,
                    error = workNameError
                )

                _uiState.update { it.copy(bottomSheetWorkNameTextField = updatedTextField) }
            }
        }
    }

    /**
     * Realiza el cálculo de la cantida de tela necesaria y lo muestra en un Dialog.
     */
    fun calculate() {
        getMeasurementsValues()?.let { mv ->
            uiState.value.garmentSubType?.createObject()
                ?.calculate(resourceProvider, mv)?.let { result ->
                    _uiState.update {
                        it.copy(
                            calculationResult = result.toString(),
                            showResultDialog = true
                        )
                    }
                }
        }
    }

    /**
     * Otiene los datos del trabajo con el id proporcionado.
     *
     * @param workId El id del trabajo.
     */
    private fun fetchWork() {
        // Sólo intentamos obtener los datos del trabajo si el id es mayor que cero
        if(workId > 0) {
            viewModelScope.launch(ioDispatcher) {
                try {
                    // Obtenemos el objeto Work con la lista de objetos Measurement
                    val workWithMeasurements = workUseCase.getWorkWithMeasurementsUseCase(workId)

                    // Si el objeto anterior no es null...
                    if(workWithMeasurements != null) {
                        // Indicamos en el WorkSession que hay un trabajo cargado
                        WorkSession.activeWorkId = workId

                        val work = workWithMeasurements.component1()
                        val measurements = workWithMeasurements.component2()
                        val measurementsTypes = work.subtype.createObject().measurements

                        // Creamos un Map con las medidas del trabajo
                        val measurementsMap = measurementsTypes.associateWith { type ->
                            MeasurementTextFieldUiState(
                                measurementType = type,
                                value = measurements.find { it.measurementType == type }?.value.toString(),
                                label = resourceProvider.getMeasurementTypeString(type),
                                placeholder = resourceProvider.getMeasurementTypeString(type)
                            )
                        }

                        // Actualizamos los datos del trabajo
                        _uiState.update {
                            it.copy(
                                workId = workId,
                                workName = work.name,
                                garmentType = work.type,
                                garmentSubType = work.subtype,
                                measurementsTextFields = measurementsMap
                            )
                        }
                    }
                } catch(ex: Exception) {
                    _uiEvent.emit(UiEvent.Error("Error: ${ex.message}"))
                }
            }
        }
    }

    /**
     * Obtiene los valores de las medidas asociadas a un subtipo de prenda de ropa.
     *
     * @return Una lista con objetos MeasurementValue o null en caso de error.
     */
    private fun getMeasurementsValues(): List<MeasurementValue>? {
        // Creamos un MutableMap a partir del Map de los campos de texto de las medidas
        val updatedMeasurementsTextFields =
            uiState.value.measurementsTextFields.toMutableMap()

        // Creamos una lista para los valores de las medidas
        val measurementsValues = mutableListOf<MeasurementValue>()

        // Creamos un validador para las medidas
        val validator = CompositeValidator(
            validators = listOf(
                MeasurementValidator(resourceProvider),
                GreaterThanZeroValidator(resourceProvider)
            )
        )

        // Variable que indica si ha habido errores
        var hasErrors = false

        // Por cada tipo de medida, obtenemos su valor y lo validamos
        uiState.value.measurementsTextFields.forEach { (measurementType, textFieldState) ->
            val value = textFieldState.value
            val error = validator.validate(value)

            // Si ha habido un error, lo mostramos en el campo de texto correspondiente...
            if(error != null) {
                updatedMeasurementsTextFields[measurementType]?.let {
                    updatedMeasurementsTextFields[measurementType] = it.copy(
                        isError = true,
                        error = error
                    )
                }

                // Indicamos que ha habido errores
                hasErrors = true
            } else {
                // ... en caso contrario, ocultamos el error...
                updatedMeasurementsTextFields[measurementType]?.let {
                    updatedMeasurementsTextFields[measurementType] = it.copy(
                        isError = false,
                        error = null
                    )
                }

                // ... y añadimos el valor a la lista
                measurementsValues.add(
                    MeasurementValue(measurementType, Util.stringToDouble(value))
                )
            }

            // Actualizamos el WorkState
            _uiState.update {
                it.copy(measurementsTextFields = updatedMeasurementsTextFields)
            }
        }

        // Retornamos null si ha habido errores o la lista en caso contrario
        return if(hasErrors) null else measurementsValues
    }

    /**
     * Obtiene un objeto Work.
     *
     * @param workId El id del objeto Work que se quiere obtener.
     * @return El objeto Work cuyo id se pasa como parámetro o null
     * en caso de que el id no exista.
     */
    private suspend fun getWork(workId: Long): Work? = withContext(ioDispatcher) {
        try {
            workUseCase.getWorkUseCase(workId)
        } catch(_: Exception) {
            null
        }
    }

    /**
     * Guarda un trabajo en la base de datos.
     *
     * @param work El objeto Work a guardar.
     * @param measurementsValues La lista de valores de las medidas
     */
    private fun saveWork(work: Work, measurementsValues: List<MeasurementValue>) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val workId = workUseCase.saveWorkUseCase(work, measurementsValues)
                _uiState.update { it.copy(workId = workId, workName = work.name) }

                // Indicamos en el WorkSession que se ha guardado un trabajo
                WorkSession.activeWorkId = workId

                // Emitimos el evento de trabajo guardado
                _uiEvent.emit(UiEvent.WorkSaved)
            } catch(ex: Exception) {
                _uiEvent.emit(UiEvent.Error("Error: ${ex.message}"))
            }
        }
    }

    /**
     * Actualiza un trabajo en la base de datos.
     *
     * @param work El objeto Work a actualizar.
     * @param measurementsValues La lista de valores de las medidas.
     */
    private fun updateWork(work: Work, measurementsValues: List<MeasurementValue>) {
        viewModelScope.launch(ioDispatcher) {
            try {
                workUseCase.updateWorkUseCase(work, measurementsValues)
                _uiState.update { it.copy(workName = work.name) }

                // Emitimos el evento de trabajo actualizado
                _uiEvent.emit(UiEvent.WorkUpdated)
            } catch(ex: Exception) {
                _uiEvent.emit(UiEvent.Error("Error: ${ex.message}"))
            }
        }
    }
}