package com.mashaal.progresstracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import com.mashaal.progresstracker.models.TaskStatus
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.ui.theme.ProgressTrackerTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MainScreen(viewModel: ProgressTrackerViewModel) {
    val currentTasks by viewModel.allTasks.collectAsState()
    val isFabVisible by viewModel.isFabVisible.collectAsState()
    val isExpanded by viewModel.expandedForFab.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset to listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collectLatest { (offset, index) ->
                viewModel.updateFabVisibility(offset, index)
            }
    }

    ProgressTrackerTheme {
        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFabVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn() + expandVertically() + slideInVertically(),
                            exit = fadeOut() + shrinkVertically() + slideOutVertically()
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                MiniFAB(viewModel, "Streak Task", Icons.Default.LocalFireDepartment, Color.Yellow, TaskStatus.Streak, "Add a Streak Task")
                                MiniFAB(viewModel, "Completed Task", Icons.Default.Check, Color.Green, TaskStatus.Completed, "Add a Completed Task")
                                MiniFAB(viewModel, "In Progress Task", Icons.Default.Timelapse, Color.Blue, TaskStatus.InProgress, "Add an In Progress Task")
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        FloatingActionButton(
                            onClick = {
                                viewModel.toggleFab()
                            },
                            modifier = Modifier.padding(20.dp),
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Menu FAB",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                state = listState
            ){
                items(currentTasks.size){ item ->
                    TaskCard(Modifier.padding(25.dp), currentTasks[item], viewModel, item)
                }
            }
            BottomSheet(currentViewModel = viewModel)
            TaskEditAlert(viewModel)
        }
    }
}