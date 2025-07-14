package com.mashaal.progresstracker.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
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
    val borderColor = when (taskData.status) {
        is Status.Completed -> Color.Green
        is Status.InProgress -> Color.Blue
        is Status.Streak -> Color.Yellow
    }

    return Card(
            modifier = modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(15),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
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
                    if (taskData.status !is Status.Completed) {
                        TextButton(onClick = {
                            currentViewModel.showAlert(taskData)
                        }) {
                            Text("Edit")
                        }
                    }else{
                        TextButton(onClick = {
                            currentViewModel.showAlert(taskData)
                        }) {
                            Text("View")
                        }
                    }
                }
                Text(
                    taskData.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    when (val status = taskData.status) {
                        is Status.Completed -> {
                            Text(
                                text = "Completed -> ${status.message}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Green,
                                softWrap = true,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f)
                            )
                        }
                        is Status.InProgress -> {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Row {
                                    Text("In Progress: ")
                                    Text(
                                        "${status.currentProgress.toInt()}%",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Blue
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { status.currentProgress / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    color = Color.Blue,
                                    trackColor = Color(0xFFBBDEFB)
                                )
                            }
                        }
                        is Status.Streak -> {
                            Text(
                                text = "${status.currentStreak} days",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Yellow,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .weight(1f)
                            )
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