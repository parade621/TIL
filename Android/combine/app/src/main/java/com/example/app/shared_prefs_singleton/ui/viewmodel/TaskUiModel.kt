package com.example.app.shared_prefs_singleton.ui.viewmodel

import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.Task

data class TaskUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)
