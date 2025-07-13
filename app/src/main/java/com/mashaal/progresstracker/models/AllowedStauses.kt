package com.mashaal.progresstracker.models

sealed class AllowedStatuses(){
    data class In_Progress(val currentProgress: Double): AllowedStatuses()
    data class Completed(val message: String): AllowedStatuses()
}