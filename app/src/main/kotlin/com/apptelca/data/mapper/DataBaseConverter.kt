package com.apptelca.data.mapper

import androidx.room.TypeConverter
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import java.time.Instant

/**
 * Clase para la conversión de tipos que debe gestionar la
 * base de datos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
class DataBaseConverter {
    /**
     * Convierte un objeto Long a uno Instant.
     *
     * @param value El valor del objeto Long.
     * @return El objeto Instant convertido.
     */
    @TypeConverter
    fun instantFromTimestamp(value: Long): Instant {
        return value.let { Instant.ofEpochMilli(it) }
    }

    /**
     * Convierte un objeto Instant a un valor Long.
     *
     * @param instant El objeto Instant a convertir.
     * @return El valor Long convertido.
     */
    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long {
        return instant.toEpochMilli()
    }

    /**
     * Convierte una constante de GarmentType a Int.
     *
     * @param garmentType La constante de enumeración GarmentType.
     * @return El valor Int de la constante de enumeración convertida.
     */
    @TypeConverter
    fun garmentTypeToInt(garmentType: GarmentType): Int {
        return garmentType.code
    }

    /**
     * Convierte un valor Int a una constante de GarmentType.
     *
     * @param value El valor Int que se convierte a GarmentType.
     * @return La constante de enumeración GarmentType convertida.
     */
    @TypeConverter
    fun intToGarmentType(value: Int): GarmentType {
        return GarmentType.entries.firstOrNull {
            it.code == value
        } ?: GarmentType.CAMISA
    }

    /**
     * Convierte una constante de GarmentSubType a Int.
     *
     * @param garmentSubType La constante de enumeración GarmentSubType.
     * @return El valor Int de la constante de enumeración convertida.
     */
    @TypeConverter
    fun garmentSubTypeToInt(garmentSubType: GarmentSubType): Int {
        return garmentSubType.code
    }

    /**
     * Convierte un valor Int a una constante de GarmentSubType.
     *
     * @param value El valor Int que se convierte a GarmentSubType.
     * @return La constante de enumeración GarmentType convertida.
     */
    @TypeConverter
    fun intToGarmentSubType(value: Int): GarmentSubType {
        return GarmentSubType.entries.firstOrNull {
            it.code == value
        } ?: GarmentSubType.CAMISA_BASICA
    }

    /**
     * Convierte una constante de MeasurementType a Int.
     *
     * @param measurementType La constante de enumeración MeasurementType.
     * @return El valor Int de la constante de enumeración convertida.
     */
    @TypeConverter
    fun measurementTypeToInt(measurementType: MeasurementType): Int {
        return measurementType.code
    }

    /**
     * Convierte un valor Int a una constante de MeasurementType.
     *
     * @param value El valor Int que se convierte a MeasurementType.
     * @return La constante de enumeración MeasurementType convertida.
     */
    @TypeConverter
    fun intToMeasurementType(value: Int): MeasurementType {
        return MeasurementType.entries.firstOrNull {
            it.code == value
        } ?: MeasurementType.ANCHO_TELA
    }
}