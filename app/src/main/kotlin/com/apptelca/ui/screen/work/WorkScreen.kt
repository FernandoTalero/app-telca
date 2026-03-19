package com.apptelca.ui.screen.work

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.apptelca.R
import com.apptelca.ui.ads.AdsViewModel
import com.apptelca.ui.component.AdMobBanner
import com.apptelca.ui.component.TAlertDialog
import com.apptelca.ui.event.UiEvent
import com.apptelca.ui.theme.AppTheme
import com.apptelca.core.Util
import kotlinx.coroutines.launch

/**
 * Crea la pantalla principal (trabajo).
 *
 * @param onNavToNote Define la acción de navegación hacia la screen de notas.
 * @param onNavToSavedWork Define la acción de navegación hacia la screen de trabajos guardados.
 * @param onNavToAbout Define la acción de navegación hacia la screen "Acerca de".
 * @param viewModel Una instancia de WorkViewModel.
 */
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun WorkScreen(
    onNavToNote: (Long) -> Unit,
    onNavToSavedWork: () -> Unit,
    onNavToAbout: () -> Unit,
    adsViewModel: AdsViewModel = hiltViewModel(),
    viewModel: WorkViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val adsUiState by adsViewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val activity = LocalActivity.current
    LaunchedEffect(Unit) {
        activity?.let {
            adsViewModel.gatherConsent(it)
        }
    }

    AppTheme(windowWidthSizeClass = Util.getWindowWidthSizeClass(LocalActivity.current)) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        // === Item de menú "Limpiar" ===
                        IconButton(onClick = {
                            // Si hay un trabajo cargado...
                            if(uiState.workId > 0) {
                                // ... mostramos el diálogo de confirmación para limpiar los datos
                                viewModel.showCleanWorkDialog()
                            } else {
                                // ... en caso contrario, limpiamos los datos directamente
                                viewModel.cleanWork()
                            }
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clean),
                                contentDescription = stringResource(R.string.tab_item_clean_description)
                            )
                        }

                        // Creamos el diálogo de confirmación para limpiar los datos del trabajo actual
                        TAlertDialog(
                            showDialog = uiState.showCleanWorkDialog,
                            title = stringResource(R.string.clean_work_confirmation_title),
                            text = stringResource(R.string.clean_work_confirmation_text),
                            onDismissRequest = { viewModel.dismissCleanWorkDialog() },
                            onDismissButtonClick = { viewModel.dismissCleanWorkDialog() },
                            onConfirmButtonClick = { viewModel.cleanWork() }
                        )

                        // === Item de menú "Guardar" ===
                        if(uiState.garmentSubType != null) {
                            IconButton(onClick = {
                                coroutineScope.launch { viewModel.showBottomSheet() }
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_save),
                                    contentDescription = stringResource(R.string.tab_item_save_description)
                                )
                            }
                        }

                        // === Menú desplegable ===
                        var expandedMenu by rememberSaveable { mutableStateOf(false) }
                        IconButton(onClick = { expandedMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.tab_item_more_description)
                            )
                        }

                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = {
                                expandedMenu = false
                            }
                        ) {
                            // === Item de menú "Notas" ===
                            // Mostramos u ocultamos el item "Notas" si hay un trabajo cargado
                            val visibleNotesItem = uiState.workId > 0
                            if(visibleNotesItem) {
                                DropdownMenuItem(
                                    text = { Text(text = stringResource(R.string.tab_item_notes)) },
                                    onClick = {
                                        expandedMenu = false
                                        onNavToNote(uiState.workId)
                                    }
                                )
                            }

                            // === Item de menú "Trabajos guardados" ===
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.tab_item_saved_works)) },
                                onClick = {
                                    expandedMenu = false
                                    onNavToSavedWork()
                                }
                            )

                            // === Item de menú "Acerca de" ===
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.tab_item_about)) },
                                onClick = {
                                    expandedMenu = false
                                    onNavToAbout()
                                }
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Si el trabajo ha sido eliminado, limpiamos los datos de la pantalla
            LaunchedEffect(WorkSession.activeWorkId) {
                if(WorkSession.activeWorkId == 0L) {
                    viewModel.cleanWork()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(paddingValues),
            ) {
                // Si se ha inicializado el SDK de AdMob, mostramos el banner publicitario
                if(adsUiState.isAdsSDKInitialized) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AdMobBanner()
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),

                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // === Encabezado ===
                    Column(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.workName.ifEmpty { stringResource(R.string.no_saved_work) },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // === Sección "1. TIPO DE PRENDA ===
                    Section(
                        titleRes = R.string.garment_type_section,
                        textRes = R.string.garment_type_section_text
                    ) {
                        // DropdownMenu con los tipos de prendas
                        GarmentTypeDropdownMenu(
                            uiState = uiState,
                            resourceProvider = viewModel.resourceProvider,
                            onItemClick = { garmentType ->
                                // Si hay un trabajo cargado...
                                if(uiState.workId > 0) {
                                    // ... mostramos el diálogo de confirmación
                                    viewModel.showUpdateGarmentTypeDialog()
                                } else {
                                    // ... en caso contrario, actualizamos el tipo de prenda directamente
                                    viewModel.updateGarmentType(garmentType)
                                }
                            },
                            content = { garmentType ->
                                TAlertDialog(
                                    showDialog = uiState.showUpdateGarmentTypeDialog,
                                    title = stringResource(R.string.change_garment_type_confirmation_title),
                                    text = stringResource(R.string.change_garment_type_confirmation_text),
                                    onDismissRequest = { viewModel.dismissUpdateGarmentTypeDialog() },
                                    onDismissButtonClick = { viewModel.dismissUpdateGarmentTypeDialog() },
                                    onConfirmButtonClick = {
                                        garmentType?.let {
                                            // Actualizamos el tipo de prenda
                                            viewModel.updateGarmentType(it)
                                            // Cerramos el diálogo
                                            viewModel.dismissUpdateGarmentTypeDialog()
                                        }
                                    }
                                )
                            }
                        )
                    }

                    // === Sección "2. MODELO" ===
                    Section(
                        titleRes = R.string.garment_model_section,
                        textRes = R.string.garment_model_section_text
                    ) {
                        // DropdownMenu con los subtipos de prendas
                        GarmentSubTypeDropdownMenu(
                            uiState = uiState,
                            resourceProvider = viewModel.resourceProvider
                        ) { garmentSubType ->
                            // Actualizamos el subtipo de prenda en el ViewModel
                            viewModel.updateGarmentSubType(garmentSubType)
                        }
                    }

                    // === Sección "3. MEDIDAS" ===
                    Section(
                        titleRes = R.string.garment_measurements_section,
                        textRes = R.string.garment_measurements_section_text
                    ) {
                        // Medidas del subtipo de prenda
                        MeasurementsTextField(uiState = uiState) { type, value ->
                            // Actualizamos la medida
                            viewModel.updateMeasurement(measurementType = type, value = value)
                        }

                        // Botón "Calcular cantidad de tela"
                        CalculateButton(uiState) { viewModel.calculate() }
                        Spacer(modifier = Modifier.height(32.dp))

                        // Dialog con el resultado del cálculo
                        ResultDialog(
                            uiState = uiState,
                            onDismissRequest = { viewModel.dismissResultDialog() },
                            onClick = { viewModel.dismissResultDialog() })
                    }
                }
            }

            // Creamos el BottomSheet para guardar o actualizar un trabajo
            SaveWorkBottomSheet(
                sheetState = sheetState,
                uiState = uiState,
                onDismiss = { viewModel.dismissBottomSheet() },
                onTextFieldValueChange = { value -> viewModel.updateBottomSheetTextField(value) },
                onCancelButtonClick = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        viewModel.dismissBottomSheet()
                    }
                },
                onSaveButtonClick = {
                    viewModel.bottomSheetSaveButtonClick(
                        onSuccess = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                viewModel.dismissBottomSheet()
                            }
                        },
                        onError = { /*Dejamos el BottomSheet abierto */ },
                        onMeasurementsError = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                viewModel.dismissBottomSheet()
                            }
                        }
                    )
                }
            )

            // Escuchamos los eventos de la UI
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
                viewModel.uiEvent.flowWithLifecycle(
                    lifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect { uiEvent ->
                    val message = when(uiEvent) {
                        // Si el trabajo ha sido guardado
                        is UiEvent.WorkSaved -> viewModel.resourceProvider
                            .getString(R.string.work_saved_message)

                        // Si el trabajo ha sido actualizado
                        is UiEvent.WorkUpdated -> viewModel.resourceProvider
                            .getString(R.string.work_updated_message)

                        // Si ha habido un error
                        is UiEvent.Error -> uiEvent.message

                        // En cualquier otro caso, retornamos una cadena de texto vacía
                        else -> ""
                    }

                    // Mostramos la Snackbar si el mensaje no está vacío
                    if(message.isNotBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = message)
                        }
                    }
                }
            }
        }
    }
}