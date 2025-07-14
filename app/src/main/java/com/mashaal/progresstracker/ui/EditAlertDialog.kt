package com.mashaal.progresstracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.Status

@Composable
fun TaskEditAlert(currentViewModel: ProgressTrackerViewModel) {
    val isSheetVisible by currentViewModel.isAlertVisible.collectAsState()
    val nameTextField by currentViewModel.alertTaskName.collectAsState()
    val descriptionTextField by currentViewModel.alertTaskDescription.collectAsState()
    val sliderPosition by currentViewModel.alertSliderPosition.collectAsState()
    val expanded by currentViewModel.alertExpanded.collectAsState()
    val selectedOption by currentViewModel.alertSelectedOption.collectAsState()
    val completedMessage by currentViewModel.completedMessage.collectAsState()
    val streakDays by currentViewModel.streakDays.collectAsState()
    val currentState = currentViewModel.taskBeingEditedState()
    val context = LocalContext.current

    val isDropdownEnabled = sliderPosition < 100f

    if (isSheetVisible) {
        AlertDialog(
            onDismissRequest = { currentViewModel.hideAlert() },
            confirmButton = if (currentState is Status.Completed) {
                {}
            } else {
                {
                    EditButton(
                        currentViewModel,
                        context,
                        "Save"
                    )
                }
            },
            dismissButton = {
                Button(onClick = { currentViewModel.hideAlert() }) {
                    Text("Cancel")
                }
            },
            title = {
                Text(when(currentState){
                    is Status.Completed -> "View Task"
                    is Status.InProgress -> "Edit Task"
                    is Status.Streak -> "Edit Task"
                    null -> "Edit Task"
                })
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = nameTextField,
                        onValueChange = { currentViewModel.updateAlertName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter task name") },
                        singleLine = true
                    )
                    TextField(
                        value = descriptionTextField,
                        onValueChange = { currentViewModel.updateAlertDescription(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter description") },
                        singleLine = true
                    )

                    if (selectedOption == currentViewModel.taskStatusOptions[0]) {
                        Text("Progress: ${sliderPosition.toInt()}%")
                        Slider(
                            value = sliderPosition,
                            onValueChange = { currentViewModel.updateAlertSlider(it) },
                            valueRange = 0f..100f
                        )
                    }
                    if (selectedOption == currentViewModel.taskStatusOptions[0]) {
                        Text("Choose Status")
                        Column {
                            Button(
                                onClick = { currentViewModel.showAlertDropDown() },
                                enabled = isDropdownEnabled
                            ) {
                                Text(selectedOption)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { currentViewModel.hideAlertDropDown() }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(currentViewModel.taskStatusOptions[0]) },
                                    onClick = {
                                        currentViewModel.updateAlertSelectedOption(currentViewModel.taskStatusOptions[0])
                                        currentViewModel.hideAlertDropDown()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(currentViewModel.taskStatusOptions[1]) },
                                    onClick = {
                                        currentViewModel.updateAlertSelectedOption(currentViewModel.taskStatusOptions[1])
                                        currentViewModel.hideAlertDropDown()
                                    }
                                )
                            }
                        }
                    } else {
                        Text("Status: $selectedOption", style = MaterialTheme.typography.titleMedium)
                    }

                    when (selectedOption) {
                        currentViewModel.taskStatusOptions[1] -> {
                            Spacer(modifier = Modifier.height(15.dp))
                            TextField(
                                value = completedMessage,
                                onValueChange = { currentViewModel.updateCompletedMessage(it) },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Enter completion message") }
                            )
                        }

                        currentViewModel.taskStatusOptions[2] -> {
                            Spacer(modifier = Modifier.height(15.dp))
                            Text("Streak Days", style = MaterialTheme.typography.titleMedium)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = {
                                        if (streakDays > 0) currentViewModel.decrementStreak()
                                    },
                                    modifier = Modifier.size(40.dp).clip(CircleShape)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }

                                Spacer(modifier = Modifier.width(20.dp))

                                Text(
                                    text = streakDays.toString(),
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Spacer(modifier = Modifier.width(20.dp))

                                IconButton(
                                    onClick = { currentViewModel.incrementStreak() },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
