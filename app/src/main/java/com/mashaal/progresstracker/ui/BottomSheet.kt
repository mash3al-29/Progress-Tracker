package com.mashaal.progresstracker.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.ValidationResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(currentViewModel: ProgressTrackerViewModel) {
    val isSheetVisible by currentViewModel.isBottomSheetVisible.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val nameTextField by currentViewModel.taskName.collectAsState()
    val descriptionTextField by currentViewModel.taskDescription.collectAsState()
    val sliderPosition by currentViewModel.sliderPosition.collectAsState()
    val expanded by currentViewModel.expanded.collectAsState()
    val selectedOption by currentViewModel.selectedOption.collectAsState()
    val context = LocalContext.current
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
                Text("Add Task")
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = nameTextField,
                    onValueChange = { value -> currentViewModel.updateTaskName(value) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter task name") },
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = descriptionTextField,
                    onValueChange = { value -> currentViewModel.updateTaskDescription(value) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter description name") },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text("Choose Status", style = MaterialTheme.typography.titleMedium)
                Column{
                    Button(onClick = { currentViewModel.showDropDown() }) {
                        Text(selectedOption)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { currentViewModel.hideDropDown() }
                    ) {
                        currentViewModel.taskStatusOptions.forEach { option ->
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

                when (selectedOption){
                    currentViewModel.taskStatusOptions[0] -> {
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
                    }
                    currentViewModel.taskStatusOptions[1] ->{
                        TextField(
                            value = currentViewModel.completedMessage.collectAsState().value,
                            onValueChange = { currentViewModel.updateCompletedMessage(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter completion message") }
                        )
                    }
                    currentViewModel.taskStatusOptions[2] ->{
                        val streakDays by currentViewModel.streakDays.collectAsState()
                        Text("Set Initial Streak Days", style = MaterialTheme.typography.titleMedium)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ){
                            IconButton(
                                onClick = { if (streakDays > 0) currentViewModel.decrementStreak() },
                                modifier = Modifier.padding(8.dp)
                            ){
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }

                            Text(
                                text = streakDays.toString(),
                                style = MaterialTheme.typography.headlineSmall
                            )

                            IconButton(
                                onClick = { currentViewModel.incrementStreak() },
                                modifier = Modifier
                                    .padding(8.dp)
                            ){
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
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
                        when (val validation = currentViewModel.validateTaskForm()) {
                            is ValidationResult.Success -> {
                                currentViewModel.saveNewTask()
                                currentViewModel.hideBottomSheet()
                            }
                            is ValidationResult.Error -> {
                                Toast.makeText(context, validation.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Text("Save Task")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
