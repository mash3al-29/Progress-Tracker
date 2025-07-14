package com.mashaal.progresstracker.models

data class Task(
    val name: String = "",
    val description: String = "",
    val status: Status
)