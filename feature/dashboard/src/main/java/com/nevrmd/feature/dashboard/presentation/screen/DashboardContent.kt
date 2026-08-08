package com.nevrmd.feature.dashboard.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nevrmd.core.ui.components.EmptyContent
import com.nevrmd.core.ui.components.ErrorContent
import com.nevrmd.core.ui.components.GlassyBackgroundGlows
import com.nevrmd.core.ui.components.LoadingContent
import com.nevrmd.core.ui.theme.LocknAlpha
import com.nevrmd.core.ui.theme.LocknSpacing
import com.nevrmd.feature.dashboard.R
import com.nevrmd.feature.dashboard.presentation.event.DashboardUiEvent
import com.nevrmd.feature.dashboard.presentation.screen.components.DeleteHabitDialog
import com.nevrmd.feature.dashboard.presentation.screen.components.HabitList
import com.nevrmd.feature.dashboard.presentation.screen.components.WeekCalendarHeader
import com.nevrmd.feature.dashboard.presentation.state.DashboardUiState

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onEvent: (DashboardUiEvent) -> Unit,
    onNavigateToHabitEditor: (habitId: String?, initialDate: String?) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    val selectedDate = when (uiState) {
        is DashboardUiState.Success -> uiState.selectedDateString
        is DashboardUiState.Empty -> uiState.selectedDateString
        else -> null
    }

    val showFab = (uiState is DashboardUiState.Success) || (uiState is DashboardUiState.Empty)

    var habitIdToDelete by remember { mutableStateOf<String?>(null) }

    if (habitIdToDelete != null) {
        DeleteHabitDialog(
            onConfirm = {
                onEvent(DashboardUiEvent.OnDeleteHabit(habitIdToDelete!!))
                habitIdToDelete = null
            },
            onDismiss = { habitIdToDelete = null }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = { onNavigateToHabitEditor(null, selectedDate) },
                    modifier = Modifier.padding(bottom = LocknSpacing.bottomBarClearance),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_habit)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            GlassyBackgroundGlows()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (uiState) {
                    is DashboardUiState.Loading -> {
                        LoadingContent(modifier = Modifier.weight(1f))
                    }

                    is DashboardUiState.Error -> {
                        ErrorContent(
                            message = uiState.message,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    is DashboardUiState.Empty -> {
                        WeekCalendarHeader(
                            selectedDateString = uiState.selectedDateString,
                            weekDays = uiState.weekDays,
                            onEvent = onEvent,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        EmptyContent(
                            message = stringResource(R.string.no_habits_for_date),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    is DashboardUiState.Success -> {
                        WeekCalendarHeader(
                            selectedDateString = uiState.selectedDateString,
                            weekDays = uiState.weekDays,
                            onEvent = onEvent,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HabitList(
                            habits = uiState.habits,
                            onEvent = onEvent,
                            onHabitClick = { habitId -> onNavigateToHabitEditor(habitId, null) },
                            onEditHabit = { habitId -> onNavigateToHabitEditor(habitId, null) },
                            onDeleteHabit = { habitId -> habitIdToDelete = habitId },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = habitIdToDelete != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = LocknAlpha.SCRIM))
                )
            }
        }
    }
}
