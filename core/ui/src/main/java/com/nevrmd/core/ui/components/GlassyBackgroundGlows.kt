package com.nevrmd.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassyBackgroundGlows(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary,
    primaryAlpha: Float = 0.12f,
    secondaryAlpha: Float = 0.12f,
    primaryAlignment: Alignment = Alignment.TopEnd,
    secondaryAlignment: Alignment = Alignment.CenterStart,
    primaryOffset: Pair<Int, Int> = Pair(50, -100),
    secondaryOffset: Pair<Int, Int> = Pair(-150, 0)
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(primaryAlignment)
                .offset(x = primaryOffset.first.dp, y = primaryOffset.second.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = primaryAlpha),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(secondaryAlignment)
                .offset(x = secondaryOffset.first.dp, y = secondaryOffset.second.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            secondaryColor.copy(alpha = secondaryAlpha),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
