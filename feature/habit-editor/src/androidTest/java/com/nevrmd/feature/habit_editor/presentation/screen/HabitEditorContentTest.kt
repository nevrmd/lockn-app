package com.nevrmd.feature.habit_editor.presentation.screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.nevrmd.domain.model.ValidationError
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorMode
import com.nevrmd.feature.habit_editor.presentation.state.HabitEditorUiState
import kotlinx.datetime.LocalDate
import org.junit.Rule
import org.junit.Test

class HabitEditorContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val baseState = HabitEditorUiState.Success(
        mode = HabitEditorMode.Create,
        emoji = "🚀",
        name = "",
        metricNoun = "minutes",
        targetAmount = "30",
        createdAt = LocalDate.parse("2024-01-01")
    )

    @Test
    fun nameValidationErrorIsDisplayed() {
        composeRule.setContent {
            HabitEditorContent(
                uiState = baseState.copy(nameError = ValidationError.Name.Empty),
                onEvent = {},
                onBack = {}
            )
        }

        composeRule.onNodeWithText("Name is required").assertExists()
    }

    @Test
    fun noValidationErrorMeansNoErrorTextShown() {
        composeRule.setContent {
            HabitEditorContent(
                uiState = baseState.copy(name = "Exercise"),
                onEvent = {},
                onBack = {}
            )
        }

        composeRule.onAllNodesWithText("Name is required").assertCountEquals(0)
    }
}
