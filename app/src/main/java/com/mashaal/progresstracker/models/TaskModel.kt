package com.mashaal.progresstracker.models

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val status: Status
)