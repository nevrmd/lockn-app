package com.nevrmd.feature.dashboard.presentation.screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nevrmd.feature.dashboard.presentation.viewmodel.DashboardEffect
import com.nevrmd.feature.dashboard.presentation.viewmodel.DashboardViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreen(
    onNavigateToHabitEditor: (habitId: String?, initialDate: String?) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    DashboardContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToHabitEditor = onNavigateToHabitEditor,
        snackbarHostState = snackbarHostState
    )
}
