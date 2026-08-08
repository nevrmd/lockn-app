package com.nevrmd.lockn.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nevrmd.lockn.presentation.components.GlassyBottomBar
import com.nevrmd.lockn.presentation.components.LocknNavHost
import com.nevrmd.lockn.presentation.components.TopLevelRoute
import com.nevrmd.navigation.Route

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topLevelRoutes = listOf(
        TopLevelRoute(
            stringResource(com.nevrmd.lockn.R.string.dashboard),
            Route.Dashboard,
            Icons.Filled.Dashboard,
            Icons.Outlined.Dashboard
        ),
        TopLevelRoute(
            stringResource(com.nevrmd.lockn.R.string.statistics),
            Route.Statistics,
            Icons.Filled.BarChart,
            Icons.Outlined.BarChart
        )
    )

    val isBottomBarVisible = topLevelRoutes.any { route ->
        currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LocknNavHost(navController = navController)

        AnimatedVisibility(
            visible = isBottomBarVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            GlassyBottomBar(
                navController = navController,
                topLevelRoutes = topLevelRoutes,
                currentDestination = currentDestination
            )
        }
    }
}
