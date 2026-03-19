package com.apptelca.ui.screen.savedwork

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.apptelca.R
import com.apptelca.ui.screen.note.NoteUiState

/**
 * Crea la TopAppBar de contexto para la selección y borrado de trabajos.
 *
 * @param itemsCount Indica la cantidad de items seleccionados.
 * @param onClose Define la acción a realizar al cerrar el TopAppBar.
 * @param onDelete Define la acción a realizar para eliminar un trabajo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedWorkContextualAppBar(
    itemsCount: Int,
    onClose: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "$itemsCount ${stringResource(R.string.tab_selected_count)}") },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.tab_item_close_description)
                )
            }
        },
        actions = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.tab_item_delete)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

/**
 * Implementa un item de lista para cada uno de los trabajos guardados.
 *
 * @param modifier El modificador a aplicar al item.
 * @param savedWorkItemUiState Objeto SavedWorkItemUiState.
 * @param onClick Define la acción a realizar cuando se pulsa sobre el item.
 * @param onLongClick Define la acción a realizar cuando en la pulsación larga.
 */
@Composable
fun SavedWorkItem(
    modifier: Modifier,
    savedWorkItemUiState: SavedWorkItemUiState,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val itemBackground =
        if(savedWorkItemUiState.isSelected) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(itemBackground)
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = savedWorkItemUiState.workName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${savedWorkItemUiState.workType} > ${savedWorkItemUiState.workSubType}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = savedWorkItemUiState.workDateTime,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}

/**
 * Crea el BottomSheet para guardar o actualizar una nota.
 *
 * @param sheetState Objeto SheetState.
 * @param uiState Objeto NoteUiState.
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
fun SaveNoteBottomSheet(
    sheetState: SheetState,
    uiState: NoteUiState,
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
                    if(uiState.currentNoteItem.id == null) stringResource(R.string.new_note_bottom_sheet_title)
                    else stringResource(R.string.update_note_bottom_sheet_title)

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
                    value = uiState.bottomSheetNoteTextField.value,
                    label = { Text(text = stringResource(R.string.save_note_text_field_label)) },
                    placeholder = { Text(text = stringResource(R.string.save_note_text_field_label)) },
                    minLines = 4,
                    maxLines = 4,
                    isError = uiState.bottomSheetNoteTextField.isError,
                    supportingText = {
                        Text(
                            text = uiState.bottomSheetNoteTextField.error ?: ""
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
















