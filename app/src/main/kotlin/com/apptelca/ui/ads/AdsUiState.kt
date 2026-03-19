package com.apptelca.ui.ads

/**
 * Almacena el estado de la UI relacionada con los anuncios.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
data class AdsUiState(
    val isAdsSDKInitialized: Boolean = false,
    val isConsentRequired: Boolean = false
)