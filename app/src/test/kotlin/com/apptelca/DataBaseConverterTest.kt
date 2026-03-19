package com.apptelca

import com.apptelca.data.mapper.DataBaseConverter
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.Instant

/**
 * Realiza pruebas unitarias locales de la clase DataBaseConverter.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class DataBaseConverterTest {
    private val dbConverter = DataBaseConverter()

    @DisplayName("Convierte un objeto Long a uno Instant")
    @Test
    fun instantFromTimestamp_isCorrect() {
        val timestamp = 1672531200000L
        val instant = dbConverter.instantFromTimestamp(timestamp)

        assertEquals(timestamp, instant.toEpochMilli())
    }

    @DisplayName("Convierte un objeto Instant a un valor Long")
    @Test
    fun instantToTimestamp_isCorrect() {
        val timestamp = 1672531200000L
        val instant = Instant.ofEpochMilli(timestamp)
        val result = dbConverter.instantToTimestamp(instant)

        assertEquals(timestamp, result)
    }

    @DisplayName("Convierte una constante de GarmentType a Int")
    @Test
    fun garmentTypeToInt_isCorrect() {
        val camisaResult = dbConverter.garmentTypeToInt(GarmentType.CAMISA)
        val faldaResult = dbConverter.garmentTypeToInt(GarmentType.FALDA)

        assertAll(
            "Verificación de tipos de prendas",
            { assertEquals(GarmentType.CAMISA.code, camisaResult) },
            { assertEquals(GarmentType.FALDA.code, faldaResult) }
        )
    }

    @DisplayName("Convierte un valor Int a una constante de GarmentType")
    @Test
    fun intToGarmentType_isCorrect() {
        val camisaResult = dbConverter.intToGarmentType(GarmentType.CAMISA.code)
        val faldaResult = dbConverter.intToGarmentType(GarmentType.FALDA.code)

        assertAll(
            "Verificación de tipos de prendas",
            { assertEquals(GarmentType.CAMISA, camisaResult) },
            { assertEquals(GarmentType.FALDA, faldaResult) }
        )
    }

    @DisplayName("Convierte una constante de GarmentSubType a Int")
    @Test
    fun garmentSubTypeToInt_isCorrect() {
        val camisaBasicaResult = dbConverter.garmentSubTypeToInt(GarmentSubType.CAMISA_BASICA)
        val faldaRectaResult = dbConverter.garmentSubTypeToInt(GarmentSubType.FALDA_RECTA)

        assertAll(
            "Verificación de subtipos de prendas",
            { assertEquals(GarmentSubType.CAMISA_BASICA.code, camisaBasicaResult) },
            { assertEquals(GarmentSubType.FALDA_RECTA.code, faldaRectaResult) }
        )
    }

    @DisplayName("Convierte un valor Int a una constante de GarmentSubType")
    @Test
    fun intToGarmentSubType_isCorrect() {
        val camisaBasicaResult = dbConverter.intToGarmentSubType(GarmentSubType.CAMISA_BASICA.code)
        val faldaRectaResult = dbConverter.intToGarmentSubType(GarmentSubType.FALDA_RECTA.code)

        assertAll(
            "Verificación de subtipos de prendas",
            { assertEquals(GarmentSubType.CAMISA_BASICA, camisaBasicaResult) },
            { assertEquals(GarmentSubType.FALDA_RECTA, faldaRectaResult) }
        )
    }

    @DisplayName("Convierte una constante de MeasurementType a Int")
    @Test
    fun measurementTypeToInt_isCorrect() {
        val anchoTelaResult = dbConverter.measurementTypeToInt(MeasurementType.ANCHO_TELA)
        val contornoPechoResult = dbConverter.measurementTypeToInt(MeasurementType.CONTORNO_PECHO)

        assertAll(
            "Verificación de tipos de medidas",
            { assertEquals(MeasurementType.ANCHO_TELA.code, anchoTelaResult) },
            { assertEquals(MeasurementType.CONTORNO_PECHO.code, contornoPechoResult) }
        )
    }

    @DisplayName("Convierte un valor Int a una constante de MeasurementType")
    @Test
    fun intToMeasurementType_isCorrect() {
        val anchoTelaResult = dbConverter.intToMeasurementType(MeasurementType.ANCHO_TELA.code)
        val contornoPechoResult =
            dbConverter.intToMeasurementType(MeasurementType.CONTORNO_PECHO.code)

        assertAll(
            "Verificación de tipos de medidas",
            { assertEquals(MeasurementType.ANCHO_TELA, anchoTelaResult) },
            { assertEquals(MeasurementType.CONTORNO_PECHO, contornoPechoResult) }
        )
    }
}