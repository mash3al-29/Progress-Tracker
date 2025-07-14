package com.mashaal.progresstracker.models

sealed class Status {
    data class InProgress(val currentProgress: Float) : Status()
    data class Completed(val message: String) : Status()
    data class Streak(val currentStreak: Int) : Status()
}