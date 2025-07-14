package com.mashaal.progresstracker.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mashaal.progresstracker.models.ValidationResult

@Composable
fun EditButton(currentViewModel: ProgressTrackerViewModel,context: Context, text: String) {
    return Button(onClick = {
        when (val validation = currentViewModel.validateAlertForm()) {
            is ValidationResult.Success -> {
                currentViewModel.hideAlert()
                currentViewModel.updateTask()
            }
            is ValidationResult.Error -> {
                Toast.makeText(context, validation.message, Toast.LENGTH_SHORT).show()
            }
        }
    }) {
        Text(text)
    }
}