package com.nevrmd.feature.dashboard.presentation.screen.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.nevrmd.feature.dashboard.presentation.model.HabitUiModel
import org.junit.Rule
import org.junit.Test

class HabitItemCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun progressBarExposesCurrentAndTargetAsStateDescription() {
        val habit = HabitUiModel(
            id = "1",
            emoji = "🚀",
            name = "Water",
            metricNoun = "glasses",
            currentAmount = 3,
            targetAmount = 5
        )

        composeRule.setContent {
            HabitItemCard(
                habit = habit,
                onClick = {},
                onEdit = {},
                onDelete = {},
                onIncrement = {}
            )
        }

        val hasExpectedStateDescription = SemanticsMatcher("has state description '3 / 5 glasses'") { node ->
            node.config.getOrNull(SemanticsProperties.StateDescription) == "3 / 5 glasses"
        }
        composeRule.onRoot().assert(hasAnyDescendant(hasExpectedStateDescription))
    }
}
