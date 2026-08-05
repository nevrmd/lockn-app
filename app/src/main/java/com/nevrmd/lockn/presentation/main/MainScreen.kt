package com.nevrmd.lockn.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nevrmd.domain.navigation.Route
import com.nevrmd.lockn.presentation.components.LocknNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topLevelRoutes = listOf(
        TopLevelRoute(stringResource(com.nevrmd.lockn.R.string.dashboard), Route.Dashboard, Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
        TopLevelRoute(stringResource(com.nevrmd.lockn.R.string.statistics), Route.Statistics, Icons.Filled.BarChart, Icons.Outlined.BarChart)
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

@Composable
private fun GlassyBottomBar(
    navController: NavHostController,
    topLevelRoutes: List<TopLevelRoute>,
    currentDestination: androidx.navigation.NavDestination?
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets.navigationBars
        ) {
            topLevelRoutes.forEach { topLevelRoute ->
                val isSelected = currentDestination?.hierarchy?.any { 
                    it.hasRoute(topLevelRoute.route::class) 
                } == true

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(topLevelRoute.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.unselectedIcon,
                            contentDescription = topLevelRoute.name
                        )
                    },
                    label = {
                        Text(
                            text = topLevelRoute.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

private data class TopLevelRoute(
    val name: String,
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
