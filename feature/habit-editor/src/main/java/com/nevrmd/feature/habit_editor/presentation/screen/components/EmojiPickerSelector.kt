package com.nevrmd.feature.habit_editor.presentation.screen.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.nevrmd.core.ui.theme.LocknAlpha

const val DEFAULT_HABIT_EMOJI = "📝"

@Composable
fun EmojiPickerSelector(
    selectedEmoji: String,
    onEmojiSelected: (String) -> Unit,
    error: String?,
    modifier: Modifier = Modifier
) {
    val transitionState = remember {
        MutableTransitionState(false)
    }
    val emojiList = listOf(DEFAULT_HABIT_EMOJI, "💧", "🏃", "🥗", "📚", "🧘", "😴", "🎸", "🌱", "💊", "🍎", "🔥")

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = LocknAlpha.PRIMARY_CONTAINER_ICON))
                .clickable { transitionState.targetState = true },
            contentAlignment = Alignment.Center
        ) {
            Text(text = selectedEmoji, fontSize = 48.sp)

            if (transitionState.currentState || transitionState.targetState) {
                Popup(
                    alignment = Alignment.Center,
                    onDismissRequest = { transitionState.targetState = false },
                    properties = PopupProperties(focusable = true)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.wrapContentSize()) {
                        androidx.compose.animation.AnimatedVisibility(
                            visibleState = transitionState,
                            enter = scaleIn(animationSpec = tween(300)) + fadeIn(),
                            exit = scaleOut(animationSpec = tween(300)) + fadeOut()
                        ) {
                            Surface(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(16.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                            ) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .size(width = 200.dp, height = 150.dp)
                                ) {
                                    items(emojiList) { emoji ->
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clickable {
                                                    onEmojiSelected(emoji)
                                                    transitionState.targetState = false
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = emoji, fontSize = 24.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
