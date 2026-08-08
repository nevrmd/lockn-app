package com.nevrmd.feature.dashboard.presentation.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.nevrmd.feature.dashboard.domain.model.DayUiModel
import com.nevrmd.feature.dashboard.presentation.model.HabitUiModel
import com.nevrmd.feature.dashboard.presentation.state.DashboardUiState
import org.junit.Rule
import org.junit.Test

class DashboardContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val weekDays = (3..9).map { day -> DayUiModel(dateString = "2024-01-0$day") }

    @Test
    fun emptyStateShowsNoHabitsMessage() {
        composeRule.setContent {
            DashboardContent(
                uiState = DashboardUiState.Empty(
                    selectedDateString = "2024-01-07",
                    weekDays = weekDays
                ),
                onEvent = {},
                onNavigateToHabitEditor = { _, _ -> }
            )
        }

        composeRule.onNodeWithText("No habits for this date.\nTap '+' to create one!").assertExists()
    }

    @Test
    fun errorStateShowsTheErrorMessage() {
        composeRule.setContent {
            DashboardContent(
                uiState = DashboardUiState.Error("Something went wrong"),
                onEvent = {},
                onNavigateToHabitEditor = { _, _ -> }
            )
        }

        composeRule.onNodeWithText("Something went wrong").assertExists()
    }

    @Test
    fun successStateShowsHabitNameAndProgress() {
        composeRule.setContent {
            DashboardContent(
                uiState = DashboardUiState.Success(
                    selectedDateString = "2024-01-07",
                    weekDays = weekDays,
                    habits = listOf(
                        HabitUiModel(
                            id = "1",
                            emoji = "🚀",
                            name = "Exercise",
                            metricNoun = "minutes",
                            currentAmount = 10,
                            targetAmount = 30
                        )
                    )
                ),
                onEvent = {},
                onNavigateToHabitEditor = { _, _ -> }
            )
        }

        composeRule.onNodeWithText("Exercise").assertExists()
        composeRule.onNodeWithText("10 / 30 minutes").assertExists()
    }
}
