package com.nevrmd.feature.statistics.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nevrmd.feature.statistics.presentation.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StatisticsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
