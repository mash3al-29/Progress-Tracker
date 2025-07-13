package com.mashaal.progresstracker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.AllowedStatuses
import com.mashaal.progresstracker.models.Task


// name, description, progress, streak days, status
@Composable
fun TaskCard(modifier: Modifier = Modifier, taskData: Task, currentViewModel: ProgressTrackerViewModel) {

    return Card(
            modifier = modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, Color.White),
            shape = RoundedCornerShape(15),
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray,
            ),
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(taskData.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier.weight(0.8f))
                    Text(
                        "${taskData.totalProgress.toString()}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = {
                        currentViewModel.showAlert()
                    }) {
                        Text("Edit")
                    }
                }
                Text(
                    taskData.description,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(modifier = Modifier.padding(top = 15.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${taskData.streakDays.toString()} days",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Green
                    )
                    Spacer(modifier.weight(0.7f))
                    when (val status = taskData.status) {
                        is AllowedStatuses.Completed -> Text(
                            "${status}%",
                            style = MaterialTheme.typography.labelMedium
                        )
                        is AllowedStatuses.In_Progress -> Text("This task is still in progress!!", style = MaterialTheme.typography.labelSmall)
                    }
                    IconButton(onClick = {

                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Increase")
                    }
                }

            }
        }
    }