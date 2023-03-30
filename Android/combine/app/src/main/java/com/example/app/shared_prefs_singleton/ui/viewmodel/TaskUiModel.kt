package com.example.app.shared_prefs_singleton.ui.viewmodel

import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.Task
import com.example.app.shared_prefs_singleton.utils.Preferences

data class TaskUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
    )
