package com.mashaal.progresstracker.models

data class Task(
    val name: String = "",
    val description: String = "",
    val totalProgress: Float = 0.0f,
    val streakDays: Int = 0,
    val status: AllowedStatuses
)