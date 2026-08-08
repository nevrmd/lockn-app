package com.nevrmd.feature.habit_editor.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nevrmd.core.ui.components.GlassyBackgroundGlows
import com.nevrmd.feature.habit_editor.R
import com.nevrmd.feature.habit_editor.presentation.event.HabitEditorUiEvent
import com.nevrmd.feature.habit_editor.presentation.mapper.asString
import com.nevrmd.feature.habit_editor.presentation.screen.components.DEFAULT_HABIT_EMOJI
import com.nevrmd.feature.habit_editor.presentation.screen.components.EmojiPickerSelector
import com.nevrmd.feature.habit_editor.presentation.screen.components.GlassyTextField
import com.nevrmd.feature.habit_editor.presentation.screen.components.TargetAmountSelector
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorMode
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditorContent(
    uiState: HabitEditorUiState.Success,
    onEvent: (HabitEditorUiEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.mode == HabitEditorMode.Create) {
                            stringResource(
                                R.string.new_habit
                            )
                        } else {
                            stringResource(R.string.edit_habit)
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            GlassyBackgroundGlows(
                primaryAlignment = Alignment.TopStart,
                secondaryAlignment = Alignment.BottomEnd,
                primaryOffset = Pair(-100, -50),
                secondaryOffset = Pair(150, 100),
                primaryAlpha = 0.15f,
                secondaryAlpha = 0.15f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmojiPickerSelector(
                    selectedEmoji = uiState.emoji.ifBlank { DEFAULT_HABIT_EMOJI },
                    onEmojiSelected = { onEvent(HabitEditorUiEvent.OnEmojiChanged(it)) },
                    error = uiState.emojiError?.asString()
                )

                Spacer(modifier = Modifier.height(32.dp))

                GlassyTextField(
                    value = uiState.name,
                    onValueChange = { onEvent(HabitEditorUiEvent.OnNameChanged(it)) },
                    label = stringResource(R.string.habit_name_label),
                    placeholder = stringResource(R.string.habit_name_placeholder),
                    error = uiState.nameError?.asString(),
                    enabled = !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                GlassyTextField(
                    value = uiState.metricNoun,
                    onValueChange = { onEvent(HabitEditorUiEvent.OnMetricNounChanged(it)) },
                    label = stringResource(R.string.metric_label),
                    placeholder = stringResource(R.string.metric_placeholder),
                    error = uiState.metricNounError?.asString(),
                    enabled = !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(32.dp))

                TargetAmountSelector(
                    amount = uiState.targetAmount,
                    metricNoun = uiState.metricNoun,
                    onAmountChanged = { onEvent(HabitEditorUiEvent.OnTargetAmountChanged(it)) },
                    error = uiState.targetAmountError?.asString(),
                    enabled = !uiState.isSaving
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onEvent(HabitEditorUiEvent.OnSaveHabit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !uiState.isSaving
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.save_habit),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}
