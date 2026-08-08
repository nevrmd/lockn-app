package com.nevrmd.feature.statistics.presentation.screen.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.nevrmd.domain.model.DailyStat
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import org.junit.Rule
import org.junit.Test

class CompletionChartTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun barExposesDayAndCompletionAsContentDescription() {
        val stats = listOf(
            DailyStat(
                date = LocalDate.parse("2024-01-02"), // a Tuesday
                completedAmount = 3,
                targetAmount = 5,
                isToday = false
            )
        ).toPersistentList()

        composeRule.setContent {
            CompletionChart(dailyStats = stats)
        }

        composeRule.onNodeWithContentDescription("Tuesday, 3 of 5 completed").assertExists()
    }
}
