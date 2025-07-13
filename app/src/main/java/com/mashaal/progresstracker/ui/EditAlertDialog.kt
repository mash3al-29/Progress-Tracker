package com.mashaal.progresstracker.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun TaskEditAlert(currentViewModel: ProgressTrackerViewModel) {
    val isSheetVisible by currentViewModel.isAlertVisible.collectAsState()
    val nameTextField by currentViewModel.alertTaskName.collectAsState()
    val descriptionTextField by currentViewModel.alertTaskDescription.collectAsState()
    val sliderPosition by currentViewModel.alertSliderPosition.collectAsState()
    val expanded by currentViewModel.alertExpanded.collectAsState()
    val selectedOption by currentViewModel.alertSelectedOption.collectAsState()
    val streakDays by currentViewModel.streakDays.collectAsState()
    val context = LocalContext.current
    val isDropdownEnabled = sliderPosition < 100f
    if (isSheetVisible) {
        AlertDialog(
            onDismissRequest = { currentViewModel.hideAlert() },
            confirmButton = {
                Button(onClick = {
                    if (nameTextField.isBlank()) {
                        Toast.makeText(context, "Task cannot have an empty name!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (selectedOption == currentViewModel.options[1] && currentViewModel.completedMessage.value.isBlank()) {
                        Toast.makeText(context, "Task cannot have an empty finish message!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    currentViewModel.hideAlert()
                    currentViewModel.updateTask()
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { currentViewModel.hideAlert() }) {
                    Text("Cancel")
                }
            },
            title = {
                Text("Edit Task")
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

                    Text("Progress: ${sliderPosition.toInt()}%")
                    Slider(
                        value = sliderPosition,
                        onValueChange = { currentViewModel.updateAlertSlider(it) },
                        valueRange = 0f..100f
                    )
                    Text("Choose Status")
                    Column {
                        Button(onClick = { if (isDropdownEnabled) currentViewModel.showAlertDropDown() },
                            enabled = isDropdownEnabled) {
                            Text(if (isDropdownEnabled) selectedOption else currentViewModel.options[1])
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { currentViewModel.hideAlertDropDown() }
                        ) {
                            currentViewModel.options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        currentViewModel.updateAlertSelectedOption(option)
                                        currentViewModel.hideAlertDropDown()
                                    }
                                )
                            }
                        }
                    }
                    if (selectedOption == currentViewModel.options[1]) {
                        Spacer(modifier = Modifier.height(15.dp))
                        TextField(
                            value = currentViewModel.completedMessage.collectAsState().value,
                            onValueChange = { currentViewModel.updateCompletedMessage(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter completion message") }
                        )
                    }
                    Text("Streak Days")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { if (streakDays > 0) currentViewModel.decrementStreak() },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
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
        )
    }
}
