package com.nevrmd.feature.habit_editor.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is HabitEditorUiState.Success -> {
            HabitEditorContent(
                uiState = state,
                onEvent = viewModel::onEvent,
                onBack = onBack
            )
        }
        is HabitEditorUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
