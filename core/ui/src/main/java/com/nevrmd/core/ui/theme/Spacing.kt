package com.nevrmd.core.ui.theme

import androidx.compose.ui.unit.dp

object LocknSpacing {
    val xxs = 4.dp
    val xs = 8.dp
    val s = 12.dp
    val m = 16.dp
    val l = 24.dp
    val xl = 32.dp

    // Clears the app's custom bottom navigation bar, which sits outside Scaffold's
    // own content padding since it isn't a standard NavigationBar/BottomAppBar.
    val bottomBarClearance = 100.dp
}

object LocknAlpha {
    const val SURFACE_VARIANT_CONTAINER = 0.3f
    const val PRIMARY_CONTAINER_ICON = 0.4f
    const val PROGRESS_TRACK = 0.1f
    const val SCRIM = 0.4f
}
