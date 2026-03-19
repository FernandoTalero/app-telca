package com.apptelca.ui.screen.note

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.apptelca.R

/**
 * Crea la TopAppBar de contexto para la selección y borrado de Notas.
 *
 * @param itemsCount Indica la cantidad de items seleccionados.
 * @param onClose Define la acción a realizar al cerrar el TopAppBar.
 * @param onDelete Define la acción a realizar para eliminar un trabajo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContextualAppBar(
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
 * Implementa un item de lista para cada una de las notas.
 *
 * @param noteItemUiState Objeto NoteItemUiState.
 * @param onClick Define la acción a realizar cuando se pulsa sobre el item.
 */
@Composable
fun NoteItem(
    modifier: Modifier,
    noteItemUiState: NoteItemUiState,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val itemBackground =
        if(noteItemUiState.isSelected) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.tertiaryContainer
    val itemForeground =
        if(noteItemUiState.isSelected) MaterialTheme.colorScheme.onErrorContainer
        else MaterialTheme.colorScheme.onTertiaryContainer

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(
            containerColor = itemBackground,
            contentColor = itemForeground
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(all = 16.dp)) {
            Text(
                text = noteItemUiState.noteDateTime,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = noteItemUiState.noteText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}