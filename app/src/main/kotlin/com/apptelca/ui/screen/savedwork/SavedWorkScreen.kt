package com.apptelca.ui.screen.savedwork

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.apptelca.R
import com.apptelca.ui.component.TAlertDialog
import com.apptelca.ui.event.UiEvent
import com.apptelca.ui.theme.AppTheme
import com.apptelca.core.Util
import kotlinx.coroutines.launch

/**
 * Crea la pantalla de trabajos guardados.
 *
 * @param onNavToWork Define la acción de navegación hacia la screen de trabajo.
 * @param onNavToBack Define la acción de navegación hacia atrás.
 * @param viewModel Una instancia de SavedWorkViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWorkScreen(
    onNavToWork: (Long) -> Unit,
    onNavToBack: () -> Unit,
    viewModel: SavedWorkViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppTheme(windowWidthSizeClass = Util.getWindowWidthSizeClass(LocalActivity.current)) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AnimatedContent(targetState = uiState.isInSelectionMode) { isInSelectionMode ->
                    if(isInSelectionMode) {
                        SavedWorkContextualAppBar(
                            itemsCount = uiState.selectedItemsCount,
                            onClose = { viewModel.closeContextualTopAppBar() },
                            onDelete = {
                                viewModel.showDeleteWorksDialog()
                            }
                        )

                        TAlertDialog(
                            showDialog = uiState.showDeleteWorksDialog,
                            title = stringResource(R.string.delete_works_confirm_title),
                            text = stringResource(R.string.delete_notes_confirm_text),
                            onDismissRequest = { viewModel.dismissDeleteWorksDialog() },
                            onDismissButtonClick = { viewModel.dismissDeleteWorksDialog() },
                            onConfirmButtonClick = { viewModel.deleteSelectedWorks() }
                        )
                    } else {
                        TopAppBar(
                            title = { Text(text = stringResource(R.string.tab_item_saved_works)) },
                            scrollBehavior = scrollBehavior,
                            navigationIcon = {
                                IconButton(onClick = onNavToBack) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.tab_back_button_description)
                                    )
                                }
                            }
                        )
                    }
                }
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = paddingValues)
            ) {
                val savedWorksItems = uiState.savedWorksItems.map { it.value }
                if(savedWorksItems.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(
                            items = savedWorksItems,
                            key = { _, item -> item.workId }) { index, item ->
                            SavedWorkItem(
                                modifier = Modifier.animateItem(
                                    fadeInSpec = tween(300),
                                    fadeOutSpec = tween(300),
                                    placementSpec = spring(stiffness = Spring.StiffnessLow)
                                ),
                                savedWorkItemUiState = item,
                                onClick = {
                                    if(uiState.isInSelectionMode) {
                                        viewModel.toggleSelection(item.workId)
                                    } else {
                                        onNavToWork(item.workId)
                                    }
                                },
                                onLongClick = {
                                    viewModel.setInSelectionMode()
                                    viewModel.toggleSelection(item.workId)
                                }
                            )

                            // Evitamos que se muestre el separador en el último item
                            if(index < savedWorksItems.lastIndex) {
                                HorizontalDivider(Modifier.padding(start = 16.dp, end = 16.dp))
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.no_saved_works_text),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Escuchamos los eventos de la UI
            val lifecycleOwner = LocalLifecycleOwner.current

            LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
                viewModel.uiEvent.flowWithLifecycle(
                    lifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).collect { uiEvent ->
                    val message = when(uiEvent) {
                        // Si el trabajo ha sido eliminado
                        is UiEvent.WorkDeleted -> "${uiEvent.deletedWorks} ${
                            viewModel.resourceProvider.getString(
                                R.string.deleted_works_message
                            )
                        }"

                        // Si ha habido un error
                        is UiEvent.Error -> uiEvent.message

                        // En cualquier otro caso, retornamos una cadena de texto vacía
                        else -> ""
                    }

                    // Mostramos la Snackbar si el mensaje no está en blanco
                    if(message.isNotBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = message)
                        }
                    }
                }
            }

            /* Capturamos el evento de navegación hacia atrás para que, en caso de que la TopAppBar
            * contextual esté activa, se cierre en lugar de volver hacia atrás. */
            BackHandler(enabled = uiState.isInSelectionMode) {
                viewModel.closeContextualTopAppBar()
            }
        }
    }
}