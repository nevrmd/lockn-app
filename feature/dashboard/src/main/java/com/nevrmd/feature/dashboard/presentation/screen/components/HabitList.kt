package com.nevrmd.feature.dashboard.presentation.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nevrmd.core.ui.theme.LocknSpacing
import com.nevrmd.feature.dashboard.presentation.event.DashboardUiEvent
import com.nevrmd.feature.dashboard.presentation.model.HabitUiModel

@Composable
fun HabitList(
    habits: List<HabitUiModel>,
    onEvent: (DashboardUiEvent) -> Unit,
    onHabitClick: (String) -> Unit,
    onEditHabit: (String) -> Unit,
    onDeleteHabit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = LocknSpacing.bottomBarClearance, top = LocknSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(LocknSpacing.xxs)
    ) {
        items(
            items = habits,
            key = { habit -> habit.id }
        ) { item ->
            HabitItemCard(
                habit = item,
                onClick = { onHabitClick(item.id) },
                onEdit = { onEditHabit(item.id) },
                onDelete = { onDeleteHabit(item.id) },
                onIncrement = {
                    onEvent(
                        DashboardUiEvent.OnIncrementHabit(
                            habitId = item.id,
                            incrementBy = 1
                        )
                    )
                }
            )
        }
    }
}
