package com.example.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.app.shared_prefs_singleton.utils.Preferences

class MyApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    @Override
    override fun onCreate() {
        super.onCreate()

        Preferences.init(this)

        context = applicationContext
    }

    @Override
    override fun attachBaseContext(base: Context) {
        Preferences.init(base)
        super.attachBaseContext(base)
    }
}