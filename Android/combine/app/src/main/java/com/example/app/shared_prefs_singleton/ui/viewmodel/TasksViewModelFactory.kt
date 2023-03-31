package com.example.app.shared_prefs_singleton.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app.shared_prefs_singleton.data.TasksRepository

//class TasksViewModelFactory(
//    private val repository: TasksRepository
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return TasksViewModel(repository) as T
//        }
//        throw java.lang.IllegalArgumentException("ViewModel not founded.....")
//    }
//}