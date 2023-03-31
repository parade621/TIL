package com.example.app.shared_prefs_singleton.data

import java.util.*

data class Task(
    val name : String,
    val deadline: Date,
    val content: String?= null,
    val priority: TaskPriority,
    val completed: Boolean = false
)
