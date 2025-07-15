package com.mashaal.progresstracker.models

enum class TaskStatus(val displayText: String) {
    InProgress("In Progress"),
    Completed("Completed"),
    Streak("Streak");
}

sealed class Status {
    data class InProgress(val currentProgress: Float) : Status()
    data class Completed(val message: String) : Status()
    data class Streak(val currentStreak: Int) : Status()
}