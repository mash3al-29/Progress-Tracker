package com.mashaal.progresstracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashaal.progresstracker.models.Task
import com.mashaal.progresstracker.ui.theme.ProgressTrackerTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MainScreen(viewModel: ProgressTrackerViewModel) {
    val currentTasks by viewModel.allTasks.collectAsState()
    val isFabVisible by viewModel.isFabVisible.collectAsState()
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
                    FloatingActionButton(
                        onClick = {
                            viewModel.showBottomSheet()
                        },
                        modifier = Modifier.padding(20.dp),
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Open Sheet")
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
                    TaskCard(Modifier.padding(25.dp), Task(
                        name = currentTasks[item].name,
                        description = currentTasks[item].description,
                        initialProgress = currentTasks[item].initialProgress,
                        streakDays = currentTasks[item].streakDays,
                        status = currentTasks[item].status
                    ), viewModel,item)
            }
            }
            BottomSheet(currentViewModel = viewModel)
            TaskEditAlert(viewModel)
        }
    }
}