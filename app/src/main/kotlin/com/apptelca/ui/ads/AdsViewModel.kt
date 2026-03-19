package com.apptelca.ui.ads

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Gestiona la UI relacionada con los anuncios.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@HiltViewModel
class AdsViewModel @Inject constructor(
    private val adsConsentManager: AdsConsentManager
) : ViewModel() {
    // StateFlow para conservar el estado de la UI
    val uiState: StateFlow<AdsUiState> = combine(
        adsConsentManager.isAdsSDKInitialized,
        adsConsentManager.isConsentRequired,
    ) { sdkInitialized, consentRequired ->
        AdsUiState(isAdsSDKInitialized = sdkInitialized, isConsentRequired = consentRequired)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdsUiState()
    )

    /**
     * Inicializa el formulario de consentimiento para los anuncios publicitarios.
     *
     * @param activity La Activity asociada a la screen.
     */
    fun gatherConsent(activity: Activity) {
        adsConsentManager.gatherConsent(activity)
    }

    /**
     * Muestra el formulario de consentimiento para los anuncios publicitarios.
     *
     * @param activity La Activity asociada a la screen.
     */
    fun showPrivacyOptionsForm(activity: Activity) {
        adsConsentManager.showPrivacyOptionsForm(activity)
    }
}
