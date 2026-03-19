package com.apptelca.ui.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gestiona la lógica relacionada con el consentimiento de los usuarios
 * respecto a la configuración de privacidad de los anuncios publicitarios.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@Singleton
class AdsConsentManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    // StateFlow para almacenar el estado de si el consentimiento del usuario es requerido
    private val _isConsentRequired = MutableStateFlow(false)
    val isConsentRequired: StateFlow<Boolean> = _isConsentRequired.asStateFlow()

    // StateFlow para almacenar el estado de la inicialización del SDK de AdMob
    private val _isAdsSDKInitialized = MutableStateFlow(false)
    val isAdsSDKInitialized: StateFlow<Boolean> = _isAdsSDKInitialized.asStateFlow()

    // ConsentInformation
    private val consentInformation = UserMessagingPlatform.getConsentInformation(context)

    /**
     * Inicializa el formulario de consentimiento para los anuncios publicitarios.
     */
    fun gatherConsent(activity: Activity) {
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        // Solicitamos la actualización de la información de consentimiento
        consentInformation.requestConsentInfoUpdate(
            activity, params,
            {
                _isConsentRequired.update {
                    consentInformation.privacyOptionsRequirementStatus ==
                            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
                }

                // Si hay disponible un formulario de consentimiento, lo mostramos si es necesario
                if(consentInformation.isConsentFormAvailable) {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                        // Tras cerrar el formulario, inicializamos AdMob
                        if(consentInformation.canRequestAds()) {
                            initSDKAds()
                        }
                    }
                }

                // Si ya podemos solicitar anuncios, inicializamos AdMob
                if(consentInformation.canRequestAds()) {
                    initSDKAds()
                }
            },
            { /* Error */ }
        )
    }

    /**
     * Muestra el formulario de consentimiento para los anuncios publicitarios.
     */
    fun showPrivacyOptionsForm(activity: Activity) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) {}
    }

    /**
     * Inicializa el SDK de AdMob.
     */
    private fun initSDKAds() {
        if(!_isAdsSDKInitialized.value) {
            MobileAds.initialize(context) { initStatus ->
                if(initStatus.adapterStatusMap.isNotEmpty()) {
                    // Indicamos que el SDK de AdMob ha sido inicializado
                    _isAdsSDKInitialized.update { true }
                }
            }
        }
    }
}