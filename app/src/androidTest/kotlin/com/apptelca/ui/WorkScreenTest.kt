package com.apptelca.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.apptelca.R
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import com.apptelca.ui.ads.AdsUiState
import com.apptelca.ui.ads.AdsViewModel
import com.apptelca.ui.screen.work.MeasurementTextFieldUiState
import com.apptelca.ui.screen.work.WorkScreen
import com.apptelca.ui.screen.work.WorkUiState
import com.apptelca.ui.screen.work.WorkViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Realiza pruebas instrumentadas para la UI de la screen Work.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@RunWith(AndroidJUnit4::class)
class WorkScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // MutableStateFlow para el estado de la UI de la screen Work
    private lateinit var workUiState: MutableStateFlow<WorkUiState>

    // Lambdas para la navegación entre pantallas
    private val onNavToNote: (Long) -> Unit = { _ -> }
    private val onNavToSavedWork: () -> Unit = {}
    private val onNavToAbout: () -> Unit = {}

    // Mocks de ViewModels
    private val adsViewModel = mockk<AdsViewModel>(relaxed = true)
    private val workViewModel = mockk<WorkViewModel>(relaxed = true)

    @Before
    fun setup() {
        // Asignamos el AdsUiState al mock del AdsViewModel
        every { adsViewModel.uiState } returns MutableStateFlow(AdsUiState()).asStateFlow()

        // Asignamos el WorkUiState al mock del WorkViewModel
        workUiState = MutableStateFlow(WorkUiState())
        every { workViewModel.uiState } returns workUiState

        // Definimos el resourceProvider para el mock de WorkViewModel
        every {
            workViewModel.resourceProvider.getString(R.string.garment_text)
        } returns context.getString(R.string.garment_text)
        every {
            workViewModel.resourceProvider.getString(R.string.model_text)
        } returns context.getString(R.string.model_text)
        every {
            workViewModel.resourceProvider.getGarmentTypeString(GarmentType.CAMISA)
        } returns context.getString(R.string.gt_camisa)
        every {
            workViewModel.resourceProvider.getGarmentSubTypeString(GarmentSubType.CAMISA_BASICA)
        } returns context.getString(R.string.gst_camisa_basica)

        // Definimos la actualización del estado de la UI al seleccionar un tipo de prenda
        every { workViewModel.updateGarmentType(any()) } answers {
            workUiState.update { it.copy(garmentType = firstArg()) }
        }
        // Definimos la actuaización del estado de la UI al seleccionar un subtipo de prenda
        every { workViewModel.updateGarmentSubType(any()) } answers {
            workUiState.update {
                it.copy(
                    garmentSubType = firstArg(),
                    measurementsTextFields = mapOf(
                        MeasurementType.ANCHO_TELA to MeasurementTextFieldUiState(
                            measurementType = MeasurementType.ANCHO_TELA,
                            label = context.getString(R.string.mt_ancho_tela)
                        )
                    )
                )
            }
        }

        // Definimos la pulsación del botón "Calcular cantidad de tela"
        every { workViewModel.calculate() } answers {
            workUiState.update {
                it.copy(
                    measurementsTextFields = mapOf(
                        MeasurementType.ANCHO_TELA to MeasurementTextFieldUiState(
                            measurementType = MeasurementType.ANCHO_TELA,
                            label = context.getString(R.string.mt_ancho_tela),
                            // Forzamos el error
                            isError = true,
                            error = context.getString(R.string.no_valid_number_error)
                        )
                    )
                )
            }
        }

        // Establecemos el contenido de la pantalla
        composeTestRule.setContent {
            WorkScreen(
                onNavToNote = onNavToNote,
                onNavToSavedWork = onNavToSavedWork,
                onNavToAbout = onNavToAbout,
                adsViewModel = adsViewModel,
                viewModel = workViewModel
            )
        }
    }

    @After
    fun unmockk() {
        unmockkAll()
    }

    @Test
    fun menuClick_isCorrect() {
        with(composeTestRule) {
            /* Comprobamos la TopAppBar a partir del clic realizado sobre el item para
            * desplegar el menú nada más abrir la aplicación. Su estado debe ser el
            * siguiente:
            * - Item "Limpiar": Mostrado
            * - Item "Guardar": No mostrado
            * - Item "Notas": No mostrado
            * - Item "Trabajos guardados": Mostrado
            * - Item "Acerca de": Mostrado
            */

            // Acción de clic sobre el item "Mostrar más opciones"
            onNodeWithContentDescription(
                context.getString(R.string.tab_item_more_description)
            ).performClick()
            // Item "Limpiar"
            onNodeWithContentDescription(
                context.getString(R.string.tab_item_clean_description)
            ).assertIsDisplayed()
            // Item "Guardar"
            onNodeWithContentDescription(
                context.getString(R.string.tab_item_save_description)
            ).assertIsNotDisplayed()
            // Item "Notas"
            onNodeWithText(context.getString(R.string.tab_item_notes)).assertIsNotDisplayed()
            // Item "Trabajos guardados"
            onNodeWithText(context.getString(R.string.tab_item_saved_works)).assertIsDisplayed()
            // Item "Acerca de"
            onNodeWithText(context.getString(R.string.tab_item_about)).assertIsDisplayed()
        }
    }

    @Test
    fun garmentTypeDropdownMenuClick_isCorrect() {
        with(composeTestRule) {
            /* Comprobamos que el clic realizado sobre el menú deplegable de los tipos de prendas
            * se despliega, mostrando las opciones disponibles. Su estado debe ser el siguiente:
            * - Menú de tipos de prendas: Mostrado (opción "Camisa" visible)
            */

            // Acción de clic sobre el menú desplegable de los tipos de prendas
            onNodeWithText(context.getString(R.string.garment_text)).performClick()
            // Opción "Camisa" mostrada
            onNodeWithText(context.getString(R.string.gt_camisa)).assertIsDisplayed()
        }
    }

    @Test
    fun garmentSubTypeDropdownMenuClick_isCorrect() {
        with(composeTestRule) {
            /* Comprobamos que el clic realizado sobre el menú desplegable de los subtipos de
            * prendas se despliega, mostrando las opciones disponibles, y que, tras seleccionar
            * una de ellas, se muestra el item de menú "Guardar". Su estado debe ser el siguiente:
            * - Menú de subtipos de prendas: Mostrado (opción "Básica" visible)
            * - Item de menú "Guardar": Mostrado
            */

            /* Acción de clic sobre el menú desplegable de los tipos de prendas para generar la
            * lista de subtipos de prendas. */
            onNodeWithText(context.getString(R.string.garment_text)).performClick()
            // Acción de clic sobre la opción "Camisa"
            onNodeWithText(context.getString(R.string.gt_camisa)).performClick()
            // Acción de clic sobre la opción "Modelo"
            onNodeWithText(context.getString(R.string.model_text)).performClick()
            // Opción "Básica" mostrada
            onNodeWithText(context.getString(R.string.gst_camisa_basica)).assertIsDisplayed()
            // Acción de clic sobre la opción "Básica"
            onNodeWithText(context.getString(R.string.gst_camisa_basica)).performClick()
            // Item de menú "Guardar"
            onNodeWithContentDescription(
                context.getString(R.string.tab_item_save_description)
            ).assertIsDisplayed()
        }
    }

    @Test
    fun calculateButtonClick_isCorrect() {
        with(composeTestRule) {
            /* Comprobamos que el clic realizado sobre el botón "Calcular cantidad de tela" muestra
            * los errores correspondientes si los campos de texto están vacíos. Su estado debe ser
            * el siguiente:
            * - Menu de subtipos de prendas: Mostrado (opción "Básica" visible)
            * - Campo de texto "Ancho de tela": Mostrado
            * - Botón "Calcular cantidad de tela": Mostrado
            * - Tras pulsar el botón, mensaje de error: Mostrado
            */

            /* Acción de clic sobre el menú desplegable de los tipos de prendas para generar la
            * lista de subtipos de prendas. */
            onNodeWithText(context.getString(R.string.garment_text)).performClick()
            // Acción de clic sobre la opción "Camisa"
            onNodeWithText(context.getString(R.string.gt_camisa)).performClick()
            // Acción de clic sobre la opción "Modelo"
            onNodeWithText(context.getString(R.string.model_text)).performClick()
            // Opción "Básica" mostrada
            onNodeWithText(context.getString(R.string.gst_camisa_basica)).assertIsDisplayed()
            // Acción de clic sobre la opción "Básica"
            onNodeWithText(context.getString(R.string.gst_camisa_basica)).performClick()
            // Campo de texto "Ancho de tela"
            onNodeWithText(context.getString(R.string.mt_ancho_tela)).assertIsDisplayed()
            // Botón "Calcular cantidad de tela"
            onNodeWithText(context.getString(R.string.btn_calculate)).assertIsDisplayed()
            // Acción de clic sobre el botón "Calcular cantidad de tela"
            onNodeWithText(context.getString(R.string.btn_calculate)).performClick()
            // Mensaje de error
            onNodeWithText(context.getString(R.string.no_valid_number_error))
                .assertIsDisplayed()
        }
    }
}