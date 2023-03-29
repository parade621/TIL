package com.example.app.shared_prefs_singleton.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app.shared_prefs_singleton.data.KeyValue
import com.example.app.shared_prefs_singleton.ui.adapter.MyPrefsAdapter
import com.example.app.shared_prefs_singleton.utils.Preferences

class MyViewModel: ViewModel() {

    private var allEntries: Map<String, *> = Preferences.preferences.all

    private var _keyValues: List<KeyValue> = allEntries.map {
        KeyValue(it.key, it.value)
    }
    val keyValues get() =_keyValues

}