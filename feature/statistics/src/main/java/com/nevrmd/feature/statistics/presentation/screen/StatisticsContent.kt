package com.nevrmd.feature.statistics.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.nevrmd.core.ui.components.EmptyContent
import com.nevrmd.core.ui.components.ErrorContent
import com.nevrmd.core.ui.components.GlassyBackgroundGlows
import com.nevrmd.core.ui.components.LoadingContent
import com.nevrmd.feature.statistics.R
import com.nevrmd.feature.statistics.presentation.event.StatisticsUiEvent
import com.nevrmd.feature.statistics.presentation.screen.components.CompletionChart
import com.nevrmd.feature.statistics.presentation.screen.components.HabitFilterChips
import com.nevrmd.feature.statistics.presentation.screen.components.MonthlyStatCard
import com.nevrmd.feature.statistics.presentation.state.StatisticsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsContent(
    uiState: StatisticsUiState,
    onEvent: (StatisticsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.statistics_title), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            GlassyBackgroundGlows(
                primaryAlignment = Alignment.TopStart,
                secondaryAlignment = Alignment.BottomEnd,
                primaryOffset = Pair(-100, -50),
                secondaryOffset = Pair(150, 100)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (uiState) {
                    is StatisticsUiState.Loading -> {
                        LoadingContent()
                    }
                    is StatisticsUiState.Error -> {
                        ErrorContent(message = uiState.message)
                    }
                    is StatisticsUiState.Success -> {
                        HabitFilterChips(
                            habits = uiState.habits,
                            selectedHabitId = uiState.selectedHabitId,
                            onHabitSelected = { onEvent(StatisticsUiEvent.OnHabitSelected(it)) }
                        )

                        if (uiState.habits.isEmpty()) {
                            EmptyContent(
                                message = stringResource(R.string.create_habits_to_see_stats),
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 24.dp)
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))

                                CompletionChart(
                                    dailyStats = uiState.weeklyStats,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                MonthlyStatCard(
                                    stat = uiState.monthlyStat,
                                    onPreviousMonth = { onEvent(StatisticsUiEvent.OnPreviousMonthClicked) },
                                    onNextMonth = { onEvent(StatisticsUiEvent.OnNextMonthClicked) },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(120.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
