package com.apptelca.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apptelca.ui.screen.about.AboutScreen
import com.apptelca.ui.screen.note.NoteScreen
import com.apptelca.ui.screen.savedwork.SavedWorkScreen
import com.apptelca.ui.screen.work.WorkScreen

/**
 * Implementa la creación de screens y la navegación entre ellas.
 */
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.Work()) {
        // Definición de la ruta Work, asociada a la screen principal
        composable<NavRoutes.Work> {
            WorkScreen(
                onNavToNote = { workId -> navController.navigate(NavRoutes.Note(workId)) },
                onNavToSavedWork = { navController.navigate(NavRoutes.SavedWork) },
                onNavToAbout = { navController.navigate(NavRoutes.About) }
            )
        }

        // Definición de la ruta Note, asociada a la screen de notas
        composable<NavRoutes.Note> {
            NoteScreen(
                onNavToBack = { navController.popBackStack() }
            )
        }

        // Definición de la ruta SavedWork, asociada a la screen de trabajos guardados
        composable<NavRoutes.SavedWork> {
            SavedWorkScreen(
                onNavToBack = { navController.popBackStack() },
                onNavToWork = { workId ->
                    navController.navigate(NavRoutes.Work(workId)) {
                        /* Eliminamos todos los destinos de la pila de navegación, incluyendo la
                        * propia pantalla del trabajo que había al inicio. */
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Definición de la ruta About, asociada a la screen "Acerca de"
        composable<NavRoutes.About> {
            AboutScreen(
                onNavToBack = { navController.popBackStack() }
            )
        }
    }
}