package com.mashaal.progresstracker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.Status
import com.mashaal.progresstracker.models.Task


@Composable
fun TaskCard(modifier: Modifier = Modifier, taskData: Task, currentViewModel: ProgressTrackerViewModel, currentIndex: Int) {
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = taskData.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Initial: ", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${taskData.initialProgress.toInt()}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (taskData.status is Status.InProgress) {
                        TextButton(onClick = {
                            currentViewModel.showAlert(taskData)
                        }) {
                            Text("Edit")
                        }
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
                        "${taskData.streakDays} days",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Green
                    )
                    Spacer(modifier.weight(0.7f))
                    when (val status = taskData.status) {
                        is Status.Completed -> Text(
                            "Completed -> ${status.message}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        is Status.InProgress -> Row{
                            Text("In Progress: ")
                            Text("${status.currentProgress.toInt()}%", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    IconButton(onClick = {
                        currentViewModel.deleteTask(currentIndex)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                    }
                }

            }
        }
    }