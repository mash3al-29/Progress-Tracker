package com.mashaal.progresstracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.AllowedStatuses
import com.mashaal.progresstracker.models.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(modifier: Modifier = Modifier, currentViewModel: ProgressTrackerViewModel) {
    val isSheetVisible by currentViewModel.isBottomSheetVisible.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val nameTextField by currentViewModel.textFieldTaskName.collectAsState()
    val descriptionTextField by currentViewModel.textFieldTaskDescription.collectAsState()
    val sliderPosition by currentViewModel.sliderPosition.collectAsState()
    val expanded by currentViewModel.expanded.collectAsState()
    val selectedOption by currentViewModel.selectedOption.collectAsState()
    val tasks by currentViewModel.allTasks.collectAsState()
    if (isSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { currentViewModel.hideBottomSheet() },
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Add a Task!", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = nameTextField,
                    onValueChange = { value -> currentViewModel.updateNameTextField(value) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter task name") },
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = descriptionTextField,
                    onValueChange = { value -> currentViewModel.updateDescriptionTextField(value) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter description name") },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Choose Initial Progress", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Progress: ${sliderPosition.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = sliderPosition,
                    onValueChange = { currentValue ->
                        currentViewModel.updateSliderPosition(currentValue)
                    },
                    valueRange = 0f..100f,
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Choose Status", style = MaterialTheme.typography.titleMedium)
                Column {
                    Button(onClick = { currentViewModel.showDropDown() }) {
                        Text(selectedOption)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { currentViewModel.hideDropDown() }
                    ) {
                        currentViewModel.options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    currentViewModel.updateSelectedOption(option)
                                    currentViewModel.hideDropDown()
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row {
                    Button(onClick = { currentViewModel.hideBottomSheet() }) {
                        Text("Close Sheet")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = {
                        val task = Task(
                            name = nameTextField,
                            description = descriptionTextField,
                            totalProgress = sliderPosition,
                            streakDays = 0,
                            status = if(selectedOption == "In Progress") AllowedStatuses.In_Progress(sliderPosition.toDouble()) else AllowedStatuses.Completed("This is completed!")
                        )
                        currentViewModel.saveNewTask(task)
                        println("${tasks} hallowwww")
                    }) {
                        Text("Save Task")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
