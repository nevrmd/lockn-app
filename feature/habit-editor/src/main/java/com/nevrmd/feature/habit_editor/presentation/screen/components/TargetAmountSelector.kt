package com.nevrmd.feature.habit_editor.presentation.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nevrmd.feature.habit_editor.R

@Composable
fun TargetAmountSelector(
    amount: String,
    metricNoun: String,
    onAmountChanged: (String) -> Unit,
    error: String?,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val currentAmount = amount.toIntOrNull() ?: 0

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.daily_target),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { if (currentAmount > 1) onAmountChanged((currentAmount - 1).toString()) },
                enabled = enabled && currentAmount > 0
            ) {
                Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.decrease))
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.length <= 4) onAmountChanged(it) },
                modifier = Modifier.width(100.dp),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = error != null,
                enabled = enabled,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )

            IconButton(
                onClick = { onAmountChanged((currentAmount + 1).toString()) },
                enabled = enabled
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.increase))
            }
        }
        
        Text(
            text = metricNoun.ifBlank { stringResource(R.string.units) },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
