package com.nevrmd.feature.dashboard.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nevrmd.feature.dashboard.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onNavigateToHabitEditor: (habitId: String?, initialDate: String?) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToHabitEditor = onNavigateToHabitEditor
    )
}