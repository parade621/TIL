package com.example.app.shared_prefs_singleton.ui.viewmodel

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.example.app.shared_prefs_singleton.data.*
import com.example.app.shared_prefs_singleton.utils.DataStoreUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class TasksViewModel : ViewModel() {
    private val keyValues: MutableLiveData<List<KeyValue>> = MutableLiveData()

    val initialSetupEvent = liveData {
        emit(DataStoreUtils.fetchInitialPreferences())
    }

    private var tasksUiModelFlow: Flow<TaskUiModel> = combine(
        TasksRepository.getNewTask(),
        DataStoreUtils.userPreferencesFlow
    ) { task: List<Task>, userPrefereneces: UserPreferences ->
        TasksRepository.forLog("viewmodel")
        return@combine TaskUiModel(
            tasks = filterSortTasks(
                task,
                userPrefereneces.showCompleted,
                userPrefereneces.sortOrder
            ),
            showCompleted = userPrefereneces.showCompleted,
            sortOrder = userPrefereneces.sortOrder
        )
    }

    var taskUiModel = tasksUiModelFlow.asLiveData()

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

    fun showCompletedTasks(show: Boolean) {
        viewModelScope.launch {
            DataStoreUtils.setShowCompletedTasks(show)
        }
    }

    fun enableSortByDeadline(enable: Boolean) {
        viewModelScope.launch {
            DataStoreUtils.setEnableSortByDeadline(enable)
        }
    }

    fun enableSortByPriority(enable: Boolean) {
        viewModelScope.launch {
            DataStoreUtils.setEnableSortByPriority(enable)
        }
    }

    fun getKeyValues(): LiveData<List<KeyValue>> {
        var allEntries: Map<Preferences.Key<*>, Any?> = emptyMap()
        viewModelScope.launch {
            DataStoreUtils.datas!!.collect { preferences ->
                allEntries = preferences.asMap()
                val list = allEntries.map {
                    KeyValue(it.key.name, it.value)
                }
                keyValues.value = list
            }
        }
        return keyValues
    }
}
