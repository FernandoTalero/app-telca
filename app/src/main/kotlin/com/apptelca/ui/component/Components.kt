package com.apptelca.ui.component

import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.apptelca.BuildConfig
import com.apptelca.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * Crea un banner publicitario de AdMob.
 */
@Composable
fun AdMobBanner() {
    val adUnitIdString = BuildConfig.BANNER_AD_UNIT_ID

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                // Asignamos el id del anuncio
                adUnitId = adUnitIdString
                // Establecemos el formato del anuncio
                setAdSize(AdSize.BANNER)
                // Creamos el AdListener
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        // Si el anuncio se carga con éxito, lo mostramos
                        visibility = View.VISIBLE
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        // Si el anuncio no se carga con éxito, lo mantenemos oculto
                        visibility = View.GONE
                    }
                }

                // Cargamos el anuncio
                loadAd(AdRequest.Builder().build())
            }
        },
        update = {}
    )
}

/**
 * Crea un menú desplegable. El tipo genérico indica el tipo del valor
 * que se asociará a cada item del menú.
 *
 * @param options Opciones del menú desplegable.
 * @param selectedText Texto de la opción seleccionada.
 * @param onItemClick Acción a realizar al seleccionar una opción.
 * @param optionalContent Contenido opcional asociado al DropdownMenu.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> TDropdownMenu(
    options: List<DropdownMenuItemUiState<T>>,
    selectedText: String,
    onItemClick: (T) -> Unit,
    optionalContent: @Composable (T?) -> Unit = {}
) {
    var expandedMenu by rememberSaveable { mutableStateOf(false) }
    var value: T? by remember { mutableStateOf(null) }

    ExposedDropdownMenuBox(
        expanded = expandedMenu,
        onExpandedChange = { expandedMenu = !expandedMenu }
    ) {
        // Campo de texto
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
        )

        // Menú desplegable
        ExposedDropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.text) },
                    onClick = {
                        expandedMenu = false

                        // Si la opción seleccionada es distinta a la actual
                        if(option.text != selectedText) {
                            /* Si el valor del item seleccinado es distinto a Unit, realizamos la
                             * acción de onItemClick. */
                            if(option.value != Unit) {
                                value = option.value
                                onItemClick(option.value)
                            }
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }

    optionalContent(value)
}

/**
 * Crea un AlertDialog.
 *
 * @param showDialog Determina la visibilidad del diálogo.
 * @param title Título del diálogo.
 * @param text Texto del diálogo.
 * @param onDismissRequest Determina la acción a realizar al cerrar el diálogo.
 * @param onDismissButtonClick Determina la acción a realizar al pulsar sobre el botón "Cancelar".
 * @param onConfirmButtonClick Determina la acción a realizar al pulsar sobre el botón "Aceptar".
 */
@Composable
fun TAlertDialog(
    showDialog: Boolean,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if(showDialog) {
        AlertDialog(
            title = { Text(text = title) },
            text = { Text(text = text) },
            onDismissRequest = onDismissRequest,
            dismissButton = {
                // Botón "Cancelar"
                Button(
                    onClick = onDismissButtonClick,
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
            },
            confirmButton = {
                // Botón "Aceptar"
                Button(
                    onClick = onConfirmButtonClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Text(text = stringResource(R.string.btn_accept))
                }
            }
        )
    }
}