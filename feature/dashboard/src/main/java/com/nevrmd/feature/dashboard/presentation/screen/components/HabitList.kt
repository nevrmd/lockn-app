package com.nevrmd.feature.dashboard.presentation.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nevrmd.domain.model.HabitWithCompletions
import com.nevrmd.feature.dashboard.presentation.event.DashboardUiEvent

@Composable
fun HabitList(
    habits: List<HabitWithCompletions>,
    onEvent: (DashboardUiEvent) -> Unit,
    onHabitClick: (String) -> Unit,
    onEditHabit: (String) -> Unit,
    onDeleteHabit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 100.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = habits,
            key = { habitWithCompletions -> habitWithCompletions.habit.id }
        ) { item ->
            HabitItemCard(
                habitWithCompletions = item,
                onClick = { onHabitClick(item.habit.id) },
                onEdit = { onEditHabit(item.habit.id) },
                onDelete = { onDeleteHabit(item.habit.id) },
                onIncrement = {
                    onEvent(
                        DashboardUiEvent.OnIncrementHabit(
                            habitId = item.habit.id,
                            incrementBy = 1
                        )
                    )
                }
            )
        }
    }
}