package com.nevrmd.lockn.presentation.components

import androidx.compose.ui.graphics.vector.ImageVector
import com.nevrmd.navigation.Route

data class TopLevelRoute(
    val name: String,
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
