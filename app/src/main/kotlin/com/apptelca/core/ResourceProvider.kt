package com.apptelca.core

import android.content.Context
import com.apptelca.R
import com.apptelca.domain.model.garment.GarmentType
import com.apptelca.domain.model.garment.subgarment.GarmentSubType
import com.apptelca.domain.model.garment.subgarment.MeasurementType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provider para obtener recursos.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Singleton
class ResourceProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    /**
     * Obtiene un recurso de texto.
     *
     * @param resourceId El id del recurso de texto que se quiere obtener.
     * @return El recurso de texto cuyo id se ha pasado como parámetro.
     */
    fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }

    /**
     * Obtiene el nombre de un tipo de prenda de ropa a partir del objeto
     * GarmentType correspondiente.
     *
     * @param garmentType El objeto GarmentType del que se quiere obtener su
     * nombre de prenda de ropa.
     * @return El nombre del tipo de prenda de ropa.
     */
    fun getGarmentTypeString(garmentType: GarmentType): String {
        return when(garmentType) {
            GarmentType.CAMISA -> getString(R.string.gt_camisa)
            GarmentType.FALDA -> getString(R.string.gt_falda)
            GarmentType.PANTALON -> getString(R.string.gt_pantalon)
            GarmentType.VESTIDO -> getString(R.string.gt_vestido)
            GarmentType.CHALECO -> getString(R.string.gt_chaleco)
            GarmentType.CHAQUETA -> getString(R.string.gt_chaqueta)
            GarmentType.ABRIGO -> getString(R.string.gt_abrigo)
        }
    }

    /**
     * Obtiene el nombre de un subtipo de prenda de ropa a partir del objeto
     * GarmentSubType correspondiente.
     *
     * @param garmentSubType El objeto GarmentSubType del que se quiere obtener su
     * nombre de prenda de ropa.
     * @return El nombre del subtipo de prenda de ropa.
     */
    fun getGarmentSubTypeString(garmentSubType: GarmentSubType): String {
        return when(garmentSubType) {
            GarmentSubType.CAMISA_BASICA -> getString(R.string.gst_camisa_basica)
            GarmentSubType.FALDA_RECTA -> getString(R.string.gst_falda_recta)
            GarmentSubType.FALDA_CAPA_ENTERA -> getString(R.string.gst_falda_de_capa_entera)
            GarmentSubType.FALDA_MEDIA_CAPA -> getString(R.string.gst_falda_de_media_capa)
            GarmentSubType.FALDA_PLISADA -> getString(R.string.gst_falda_plisada)
            GarmentSubType.PANTALON_BASICO -> getString(R.string.gst_pantalon_basico)
            GarmentSubType.VESTIDO_RECTO -> getString(R.string.gst_vestido_recto)
            GarmentSubType.VESTIDO_RECTO_SIN_MANGAS -> getString(R.string.gst_vestido_recto_sin_mangas)
            GarmentSubType.VESTIDO_CAPA_ENTERA -> getString(R.string.gst_vestido_de_capa_entera)
            GarmentSubType.VESTIDO_CAPA_ENTERA_SIN_MANGAS -> getString(R.string.gst_vestido_de_capa_entera_sin_mangas)
            GarmentSubType.VESTIDO_MEDIA_CAPA -> getString(R.string.gst_vestido_de_media_capa)
            GarmentSubType.VESTIDO_MEDIA_CAPA_SIN_MANGAS -> getString(R.string.gst_vestido_de_media_capa_sin_mangas)
            GarmentSubType.VESTIDO_PLISADO -> getString(R.string.gst_vestido_plisado)
            GarmentSubType.VESTIDO_PLISADO_SIN_MANGAS -> getString(R.string.gst_vestido_plisado_sin_mangas)
            GarmentSubType.CHALECO_BASICO -> getString(R.string.gst_chaleco_basico)
            GarmentSubType.CHAQUETA_BASICA -> getString(R.string.gst_chaqueta_basica)
            GarmentSubType.ABRIGO_RECTO -> getString(R.string.gst_abrigo_recto)
            GarmentSubType.ABRIGO_CRUZADO -> getString(R.string.gst_abrigo_cruzado)
        }
    }

    /**
     * Obtiene el nombre de un tipo de medida a partir del objeto MeasurementType
     * correspondiente.
     *
     * @param measurementType El objeto MeasurementType del que se quiere obtener su nombre.
     * @return El nombre del tipo de medida.
     */
    fun getMeasurementTypeString(measurementType: MeasurementType): String {
        return when(measurementType) {
            MeasurementType.ANCHO_TELA -> getString(R.string.mt_ancho_tela)
            MeasurementType.CONTORNO_PECHO -> getString(R.string.mt_contorno_pecho)
            MeasurementType.CONTORNO_CINTURA -> getString(R.string.mt_contorno_cintura)
            MeasurementType.CONTORNO_CADERA -> getString(R.string.mt_contorno_cadera)
            MeasurementType.CONTORNO_MANGA -> getString(R.string.mt_contorno_manga)
            MeasurementType.LARGO_TALLE -> getString(R.string.mt_largo_talle)
            MeasurementType.LARGO_MANGA -> getString(R.string.mt_largo_manga)
            MeasurementType.LARGO_CAMISA -> getString(R.string.mt_largo_camisa)
            MeasurementType.LARGO_FALDA -> getString(R.string.mt_largo_falda)
            MeasurementType.LARGO_PANTALON -> getString(R.string.mt_largo_pantalon)
            MeasurementType.LARGO_CHAQUETA -> getString(R.string.mt_largo_chaqueta)
            MeasurementType.LARGO_ABRIGO -> getString(R.string.mt_largo_abrigo)
        }
    }
}