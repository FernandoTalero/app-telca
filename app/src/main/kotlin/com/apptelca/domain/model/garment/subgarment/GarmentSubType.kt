package com.apptelca.domain.model.garment.subgarment

import com.apptelca.domain.model.GarmentSubTypeBase

/**
 * Enumaración con los subtipos de prendas de ropa.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
enum class GarmentSubType(c: Int) : GarmentSubTypeBase {
    // Camisa
    CAMISA_BASICA(1),

    // Falda
    FALDA_RECTA(2),
    FALDA_CAPA_ENTERA(3),
    FALDA_MEDIA_CAPA(4),
    FALDA_PLISADA(5),

    // Pantalón
    PANTALON_BASICO(6),

    // Vestido
    VESTIDO_RECTO(7),
    VESTIDO_RECTO_SIN_MANGAS(8),
    VESTIDO_CAPA_ENTERA(9),
    VESTIDO_CAPA_ENTERA_SIN_MANGAS(10),
    VESTIDO_MEDIA_CAPA(11),
    VESTIDO_MEDIA_CAPA_SIN_MANGAS(12),
    VESTIDO_PLISADO(13),
    VESTIDO_PLISADO_SIN_MANGAS(14),

    // Chaleco
    CHALECO_BASICO(15),

    // Chaqueta
    CHAQUETA_BASICA(16),

    // Abrigo
    ABRIGO_RECTO(17),
    ABRIGO_CRUZADO(18);

    // Propiedad que define un código interno para cada constante
    val code: Int = c

    /**
     * Crea un objeto correspondiente para cada constante de la enumeración.
     *
     * @return Devuelve un objeto SubGarment.
     */
    override fun createObject(): SubGarment {
        return when(this) {
            CAMISA_BASICA -> CamisaBasica()
            FALDA_RECTA -> FaldaRecta()
            FALDA_CAPA_ENTERA -> FaldaCapaEntera()
            FALDA_MEDIA_CAPA -> FaldaMediaCapa()
            FALDA_PLISADA -> FaldaPlisada()
            PANTALON_BASICO -> PantalonBasico()
            VESTIDO_RECTO -> VestidoRecto()
            VESTIDO_RECTO_SIN_MANGAS -> VestidoRectoSinMangas()
            VESTIDO_CAPA_ENTERA -> VestidoCapaEntera()
            VESTIDO_CAPA_ENTERA_SIN_MANGAS -> VestidoCapaEnteraSinMangas()
            VESTIDO_MEDIA_CAPA -> VestidoMediaCapa()
            VESTIDO_MEDIA_CAPA_SIN_MANGAS -> VestidoMediaCapaSinMangas()
            VESTIDO_PLISADO -> VestidoPlisado()
            VESTIDO_PLISADO_SIN_MANGAS -> VestidoPlisadoSinMangas()
            CHALECO_BASICO -> ChalecoBasico()
            CHAQUETA_BASICA -> ChaquetaBasica()
            ABRIGO_RECTO -> AbrigoRecto()
            ABRIGO_CRUZADO -> AbrigoCruzado()
        }
    }
}