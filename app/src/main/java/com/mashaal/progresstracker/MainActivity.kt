package com.mashaal.progresstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mashaal.progresstracker.ui.MainScreen
import com.mashaal.progresstracker.ui.ProgressTrackerViewModel
import com.mashaal.progresstracker.ui.theme.ProgressTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var viewModel: ProgressTrackerViewModel = ProgressTrackerViewModel()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(Modifier, viewModel)
        }
    }
}