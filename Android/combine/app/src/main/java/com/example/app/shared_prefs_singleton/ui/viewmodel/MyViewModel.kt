package com.example.app.shared_prefs_singleton.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.shared_prefs_singleton.data.KeyValue
import com.example.app.shared_prefs_singleton.utils.Preferences

class MyViewModel : ViewModel() {

    private var allEntries: Map<String, *> = Preferences.preferences.all
    private val keyValues: MutableLiveData<List<KeyValue>> = MutableLiveData()

    init {
        val list = allEntries.map {
            KeyValue(it.key, it.value)
        }
        keyValues.value = list
    }

    fun getKeyValues(): LiveData<List<KeyValue>> {
        return keyValues
    }

}