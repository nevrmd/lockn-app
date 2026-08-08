package com.nevrmd.lockn.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nevrmd.feature.dashboard.presentation.screen.DashboardScreen
import com.nevrmd.feature.habit_editor.presentation.screen.HabitEditorScreen
import com.nevrmd.feature.statistics.presentation.screen.StatisticsScreen
import com.nevrmd.navigation.Route

@Composable
fun LocknNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Route.Dashboard
    ) {
        composable<Route.Dashboard> {
            DashboardScreen(
                onNavigateToHabitEditor = { habitId, initialDate ->
                    navController.navigate(Route.HabitEditor(habitId, initialDate))
                }
            )
        }

        composable<Route.HabitEditor> {
            HabitEditorScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Route.Statistics> {
            StatisticsScreen()
        }
    }
}
