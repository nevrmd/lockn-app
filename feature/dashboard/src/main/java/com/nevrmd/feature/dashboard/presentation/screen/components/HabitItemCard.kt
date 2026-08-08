package com.nevrmd.feature.dashboard.presentation.screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevrmd.core.ui.theme.LocknAlpha
import com.nevrmd.feature.dashboard.R
import com.nevrmd.feature.dashboard.presentation.model.HabitUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitItemCard(
    habit: HabitUiModel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentCount = habit.currentAmount
    val targetCount = habit.targetAmount
    val progress = if (targetCount > 0) (currentCount.toFloat() / targetCount).coerceIn(0f, 1f) else 0f

    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showMenu = true }
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = LocknAlpha.SURFACE_VARIANT_CONTAINER
                )
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = LocknAlpha.PRIMARY_CONTAINER_ICON)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = habit.emoji, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$currentCount / $targetCount ${habit.metricNoun}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .semantics {
                                stateDescription = "$currentCount / $targetCount ${habit.metricNoun}"
                            },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = LocknAlpha.PROGRESS_TRACK)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.increment),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .clip(RoundedCornerShape(16.dp))
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = {
                    showMenu = false
                    onEdit()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) },
                onClick = {
                    showMenu = false
                    onDelete()
                },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}
