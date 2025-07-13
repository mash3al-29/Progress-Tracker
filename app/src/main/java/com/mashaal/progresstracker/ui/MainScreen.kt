package com.mashaal.progresstracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.AllowedStatuses
import com.mashaal.progresstracker.models.Task
import com.mashaal.progresstracker.ui.theme.ProgressTrackerTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: ProgressTrackerViewModel) {
    val currentTasks = viewModel.allTasks.collectAsState()
    ProgressTrackerTheme {
        Scaffold(  floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.showBottomSheet()
                },
                modifier = Modifier.padding(20.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Open Sheet")
            }
        },
            floatingActionButtonPosition = FabPosition.End,
            modifier = Modifier.fillMaxSize(),
            ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ){
                items(currentTasks.value.size){ item ->
                    TaskCard(Modifier.padding(25.dp), Task(
                        name = currentTasks.value[item].name,
                        description = currentTasks.value[item].description,
                        totalProgress = currentTasks.value[item].totalProgress,
                        streakDays = currentTasks.value[item].streakDays,
                        status = currentTasks.value[item].status
                    ), viewModel)
            }
            }
            BottomSheet(currentViewModel = viewModel)
            TaskEditAlert(modifier = Modifier, viewModel)
        }
    }
}