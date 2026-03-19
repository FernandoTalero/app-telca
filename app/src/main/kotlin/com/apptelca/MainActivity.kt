package com.apptelca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apptelca.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity de la arquitectura de actividad única.
 *
 * @author Fernando Talero
 * @version 1.0
 * @since 1.0
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
}