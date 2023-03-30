package com.example.app.shared_prefs_singleton.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.Task
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.shared_prefs_singleton.utils.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class TasksViewModel(
    repository: TasksRepository,
) : ViewModel() {

    // 변경된 필터를 스트림으로 유지
    // private val _showCompletedFlow = MutableStateFlow(false)
    private val _showCompletedFlow = Preferences.showCompleted

    // 정렬 순서 변경 사항을 스트림으로 유지
    private val sortOrderFlow = Preferences.sortOrderFlow

    private val tasksUiModelFlow: Flow<TaskUiModel> = combine(
        repository.tasks,
        _showCompletedFlow,
        sortOrderFlow
    ) { tasks: List<Task>, showCompleted: Boolean, sortOrder: SortOrder ->
        Log.d("myTasksViewModel","이거 왜안됨? ${showCompleted} and ${sortOrder}")
        return@combine TaskUiModel(
            tasks = filterSortTasks(tasks, showCompleted, sortOrder),
            showCompleted = showCompleted,
            sortOrder = sortOrder
        )
    }

    val taskUiModel = tasksUiModelFlow.asLiveData()

    private fun filterSortTasks(
        tasks: List<Task>,
        showCompleted: Boolean,
        sortOrder: SortOrder
    ): List<Task> {
        val filteredTask = if (showCompleted) {
            tasks
        } else {
            tasks.filter { !it.completed }
        }
        return when (sortOrder) {
            SortOrder.NONE -> filteredTask
            SortOrder.BY_DEADLINE -> filteredTask.sortedByDescending { it.deadline }
            SortOrder.BY_PRIORITY -> filteredTask.sortedBy { it.priority }
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTask.sortedWith(
                compareByDescending<Task> { it.deadline }.thenBy { it.priority }
            )
        }
    }

    fun showCompletedTasks(show:Boolean){
        Log.d("myTasksViewModel", show.toString())
        Preferences.showCompletedTasks(show)
    }

    fun enableSortByDeadline(enable: Boolean){
        Preferences.enableSortByDeadLine(enable)
    }
    fun enableSortByPriority(enable: Boolean){
        Preferences.enableSortByPriority(enable)
    }
}
