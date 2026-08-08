package com.nevrmd.feature.habit_editor.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nevrmd.core.ui.components.ErrorContent
import com.nevrmd.core.ui.components.LoadingContent
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorUiState
import com.nevrmd.feature.habit_editor.presentation.viewmodel.HabitEditorEffect
import com.nevrmd.feature.habit_editor.presentation.viewmodel.HabitEditorViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HabitEditorScreen(
    onBack: () -> Unit,
    viewModel: HabitEditorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { effect ->
            when (effect) {
                HabitEditorEffect.HabitSaved -> onBack()
            }
        }
    }

    when (val state = uiState) {
        is HabitEditorUiState.Loading -> {
            LoadingContent()
        }
        is HabitEditorUiState.Success -> {
            HabitEditorContent(
                uiState = state,
                onEvent = viewModel::onEvent,
                onBack = onBack
            )
        }
        is HabitEditorUiState.Error -> {
            ErrorContent(message = state.message)
        }
    }
}
