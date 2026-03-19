package com.apptelca.ui.screen.work

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.apptelca.R
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import com.apptelca.core.ResourceProvider
import com.apptelca.ui.component.DropdownMenuItemUiState
import com.apptelca.ui.component.TDropdownMenu
import kotlinx.coroutines.delay

/**
 * Crea una sección en la pantalla Work.
 *
 * @param titleRes El recurso de cadena del título de la sección.
 * @param textRes El recurso de cadena del texto de la sección.
 * @param content El contenido de la sección.
 */
@Composable
fun Section(
    @StringRes titleRes: Int,
    @StringRes textRes: Int,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(titleRes),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodyMedium
        )

        // Añadimos el contenido de la sección
        content()
    }
}

/**
 * Crea el menú desplegable para la lista de tipos de prendas de ropa.
 *
 * @param uiState Objeto WorkUIState.
 * @param resourceProvider Objeto ResourceProvider para obtener recursos.
 * @param onItemClick Acción que se realiza al pulsar sobre un item del DropdownMenu.
 * @param content Contenido asociado al DropdownMenu del tipo de prenda de ropa.
 */
@Composable
fun GarmentTypeDropdownMenu(
    uiState: WorkUiState,
    resourceProvider: ResourceProvider,
    onItemClick: (GarmentType) -> Unit,
    content: @Composable (GarmentType?) -> Unit
) {
    val options: List<DropdownMenuItemUiState<GarmentType>> = GarmentType.entries.map {
        DropdownMenuItemUiState(
            text = resourceProvider.getGarmentTypeString(it),
            value = it
        )
    }

    /* Establecemos el texto del campo de texto de la lista, el cual se inicializa a partir
    * del tipo de prenda si no es null o, en caso contrario, el texto predeterminado. */
    val selectedText = uiState.garmentType?.let {
        resourceProvider.getGarmentTypeString(it)
    } ?: resourceProvider.getString(R.string.garment_text)

    TDropdownMenu(
        options = options,
        selectedText = selectedText,
        onItemClick = { value -> onItemClick(value) },
        optionalContent = { value -> content(value) }
    )
}

/**
 * Crea el menú desplegable para la lista de subtipos de prendas de ropa.
 *
 * @param uiState Objeto WorkState.
 * @param resourceProvider Objeto ResourceProvider para obtener recursos.
 * @param onItemClick Acción que se realiza al pulsar sobre un item del DropdownMenu.
 */
@Composable
fun GarmentSubTypeDropdownMenu(
    uiState: WorkUiState,
    resourceProvider: ResourceProvider,
    onItemClick: (GarmentSubType) -> Unit
) {
    val options: List<DropdownMenuItemUiState<GarmentSubType>> =
        uiState.garmentType?.createObject()?.garmentSubTypes?.map {
            DropdownMenuItemUiState(
                text = resourceProvider.getGarmentSubTypeString(it),
                value = it
            )
        } ?: emptyList()

    /* Establecemos el texto del campo de texto de la lista, el cual se inicializa a partir
    * del subtipo de prenda si no es null o, en caso contrario, el texto predeterminado. */
    val selectedText = uiState.garmentSubType?.let {
        resourceProvider.getGarmentSubTypeString(it)
    } ?: resourceProvider.getString(R.string.model_text)

    TDropdownMenu(
        options = options,
        selectedText = selectedText,
        onItemClick = { value -> onItemClick(value) }
    )
}

/**
 * Crea los campos de texto para las medidas de los subtipos de prendas
 * de ropa.
 *
 * @param uiState Objeto WorkState.
 * @param onValueChange Acción que se realiza al cambiar el valor de los campos de texto.
 */
@Composable
fun MeasurementsTextField(
    uiState: WorkUiState,
    onValueChange: (MeasurementType, String) -> Unit
) {
    // Opciones y acciones de teclado
    val focusManager = LocalFocusManager.current
    val keyboardOptionsNext = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next
    )
    val keyboardOptionsDone = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Done
    )
    val keyboardActionsNext = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    val keyboardActionsDone = KeyboardActions(
        onDone = { focusManager.clearFocus() }
    )

    Spacer(modifier = Modifier.height(4.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(all = 16.dp)
    ) {
        // Si measurements no es un Map vacío, creamos un campo de texto para cada medida...
        if(uiState.measurementsTextFields.isNotEmpty()) {
            uiState.measurementsTextFields.onEachIndexed { index, (type, state) ->
                // Añadimos una animación al mostrar y ocultar los campos de texto
                var visible by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(index * 10L)
                    visible = true
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + expandVertically()
                ) {
                    val keyboardOptions: KeyboardOptions
                    val keyboardActions: KeyboardActions

                    if(index < uiState.measurementsTextFields.size - 1) {
                        keyboardOptions = keyboardOptionsNext
                        keyboardActions = keyboardActionsNext
                    } else {
                        keyboardOptions = keyboardOptionsDone
                        keyboardActions = keyboardActionsDone
                    }

                    // Campo de texto
                    OutlinedTextField(
                        value = state.value,
                        onValueChange = { changedValue -> onValueChange(type, changedValue) },
                        label = { Text(text = state.label) },
                        placeholder = { Text(text = state.placeholder) },
                        isError = state.isError,
                        supportingText = {
                            if(state.isError) {
                                state.error?.let { Text(text = it) }
                            }
                        },
                        trailingIcon = {
                            if(state.isError) {
                                Icon(
                                    Icons.Filled.Info,
                                    contentDescription = stringResource(R.string.text_field_error_description),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions
                    )
                }
            }
        } else {
            // ... en caso contrario, mostramos un texto explicativo
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.no_measurements_text),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

/**
 * Crea el botón para calcular la cantidad de tela y el diálogo con el resultado
 * del cálculo.
 *
 * @param uiState Objeto WorkState.
 * @param onClick Acción que se lleva a al pulsa el botón
 */
@Composable
fun CalculateButton(
    uiState: WorkUiState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(uiState.garmentSubType != null) {
            Button(onClick = onClick) {
                Text(text = stringResource(R.string.btn_calculate))
            }
        }
    }
}

/**
 * Crea un Dialog para mostrar el resultado del cálculo de la cantidad de tela necesaria.
 *
 * @param uiState Objeto WorkState.
 * @param onDismissRequest Acción que se lleva a cabo al ocultar el diálogo.
 * @param onClick Acción que se lleva a cabo al pulsar el botón del diálogo.
 */
@Composable
fun ResultDialog(
    uiState: WorkUiState,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit
) {
    if(uiState.showResultDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // === Título ===
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.calculation_result_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // === Texto ===
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = AnnotatedString.fromHtml(uiState.calculationResult),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    // === Botón ===
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(22.dp),
                    ) {
                        Text(text = stringResource(R.string.btn_accept))
                    }
                }
            }
        }
    }
}

/**
 * Crea el BottomSheet para guardar o actualizar un trabajo.
 *
 * @param sheetState Objeto SheetState.
 * @param uiState Objeto WorkUiState.
 * @param onDismiss Acción que se lleva a cabo al cerrar el BottomSheet.
 * @param onTextFieldValueChange Acción que se lleva a cabo al cambiar el valor del campo
 * de texto del BottomSheet.
 * @param onCancelButtonClick Acción que se lleva a cabo al pulsar el botón "Cancelar"
 * del BottomSheet.
 * @param onSaveButtonClick Acción que se lleva a cabo al pulsar el botón "Guardar"
 * del BottomSheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveWorkBottomSheet(
    sheetState: SheetState,
    uiState: WorkUiState,
    onDismiss: () -> Unit,
    onTextFieldValueChange: (String) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    if(uiState.showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bottomSheetTitle =
                    if(uiState.workId == 0L) stringResource(R.string.new_work_bottom_sheet_title)
                    else stringResource(R.string.update_work_bottom_sheet_title)

                // === Título ===
                Text(
                    text = bottomSheetTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // === Campo de texto ===
                OutlinedTextField(
                    value = uiState.bottomSheetWorkNameTextField.value,
                    label = { Text(text = stringResource(R.string.save_work_text_field_label)) },
                    placeholder = { Text(text = stringResource(R.string.save_work_text_field_label)) },
                    isError = uiState.bottomSheetWorkNameTextField.isError,
                    supportingText = {
                        Text(
                            text = uiState.bottomSheetWorkNameTextField.error ?: ""
                        )
                    },
                    onValueChange = { changedValue -> onTextFieldValueChange(changedValue) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                // === Botones ===
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // === Botón "Cancelar" ===
                    Button(
                        onClick = onCancelButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(text = stringResource(R.string.btn_cancel))
                    }

                    // === Botón "Guardar" ===
                    Button(
                        onClick = onSaveButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        Text(text = stringResource(R.string.btn_save))
                    }
                }
            }
        }
    }
}