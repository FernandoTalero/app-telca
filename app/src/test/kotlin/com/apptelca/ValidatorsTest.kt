package com.apptelca

import com.apptelca.core.ResourceProvider
import com.apptelca.validator.GreaterThanZeroValidator
import com.apptelca.validator.MeasurementValidator
import com.apptelca.validator.MeasurementValueValidator
import com.apptelca.validator.NoteTextValidator
import com.apptelca.validator.NoteTextValueValidator
import com.apptelca.validator.WorkNameValidator
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * Realiza pruebas unitarias locales de las clases Validator.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas unitarias locales de las clases validadoras")
class ValidatorsTest {
    private val resourceProvider = mockk<ResourceProvider>()

    @BeforeEach
    fun setupResourceProvider() {
        // Definimos la función resourceProvider.getString()
        every { resourceProvider.getString(any()) } returns "Mensaje de error"
    }

    @AfterEach
    fun unmockk() {
        unmockkAll()
    }

    @DisplayName("Número Double mayor de cero")
    @ParameterizedTest
    @CsvSource("0, 10.00", "1, 0") // Valor correcto / valor incorrecto
    fun greaterThanZeroValidator_isCorrect(index: Int, text: String) {
        val validator = GreaterThanZeroValidator(resourceProvider)
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }

    @DisplayName("Texto de menos de siete caracteres y convertible a un número decimal")
    @ParameterizedTest
    @CsvSource("0, 10.00", "1, 1234567") // Valor correcto / valor incorrecto
    fun measurementValidator_isCorrect(index: Int, text: String) {
        val validator = MeasurementValidator(resourceProvider)
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }

    @DisplayName("Texto de menos de siete caracteres")
    @ParameterizedTest
    @CsvSource("0, 10", "1, 1234567") // Valor correcto / valor incorrecto
    fun measurementValueValidator_isCorrect(index: Int, text: String) {
        val validator = MeasurementValueValidator(resourceProvider)
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }

    @DisplayName("Texto de 200 caracteres como máximo y no vacío")
    @ParameterizedTest
    @CsvSource(
        "0, Texto de la nota", "1, Texto de la notaTexto de la notaTexto de la nota" +
                "Texto de la notaTexto de la notaTexto de la notaTexto de la notaTexto de la notaTexto " +
                "de la notaTexto de la notaTexto de la notaTexto de la notaTexto de la notaTexto de la " +
                "notaTexto de la notaTexto de la nota", "2, ''"
    ) // Valor correcto / valores incorrectos
    fun noteTextValidator_isCorrect(index: Int, text: String) {
        val validator = NoteTextValidator()
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }

    @DisplayName("Texto de 200 caracteres como máximo")
    @ParameterizedTest
    @CsvSource(
        "0, Texto de la nota", "1, Texto de la notaTexto de la notaTexto de la nota" +
                "Texto de la notaTexto de la notaTexto de la notaTexto de la notaTexto de la notaTexto " +
                "de la notaTexto de la notaTexto de la notaTexto de la notaTexto de la notaTexto de la " +
                "notaTexto de la notaTexto de la nota"
    ) // Valor correcto / valor incorrecto
    fun noteTextValueValidator_isCorrect(index: Int, text: String) {
        val validator = NoteTextValueValidator()
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }

    @DisplayName("Texto de 50 caracteres como máximo y no vacío")
    @ParameterizedTest
    @CsvSource(
        "0, Nombre del trabajo", "1, Nombre del trabajoNombre del trabajoNombre del" +
                " trabajoNombre del trabajoNombre del trabajoNombre del trabajoNombre del trabajo" +
                "Nombre del trabajo", "2, ''"
    ) // Valor correcto / valores incorrectos
    fun workNameValidator_isCorrect(index: Int, text: String) {
        val validator = WorkNameValidator()
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }

    @DisplayName("Texto de 50 caracteres como máximo")
    @ParameterizedTest
    @CsvSource(
        "0, Nombre del trabajo", "1, Nombre del trabajoNombre del trabajoNombre del" +
                " trabajoNombre del trabajoNombre del trabajoNombre del trabajoNombre del trabajo" +
                "Nombre del trabajo"
    ) // Valor correcto / valor incorrecto
    fun workNameValueValidator_isCorrect(index: Int, text: String) {
        val validator = WorkNameValidator()
        if(index == 0)
            assertNull(validator.validate(text))
        else
            assertNotNull(validator.validate(text))
    }
}