package com.mashaal.progresstracker.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.TaskStatus

@Composable
fun MiniFAB(viewModel: ProgressTrackerViewModel, nameTask: String, currentIcon: ImageVector, color: Color, currentTaskStatus: TaskStatus, contentDescription: String) {
    return Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            nameTask,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.8f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SmallFloatingActionButton(
            onClick = {
                viewModel.handleMiniFabClick(currentTaskStatus)
            },
            containerColor = color,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(currentIcon, contentDescription = contentDescription)
        }
    }
}