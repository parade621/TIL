package com.example.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.app.shared_prefs_singleton.utils.DatabaseManager
import com.example.app.shared_prefs_singleton.utils.DataStoreManager
import com.example.app.shared_prefs_singleton.utils.MyPreferences

class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    @Override
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        MyPreferences.init(this)
        DataStoreManager.init(this)
        DatabaseManager.init(this)
    }
    @Override
    override fun attachBaseContext(base: Context) {
        MyPreferences.init(base)
        super.attachBaseContext(base)
    }
}