package com.example.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClient
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl
import com.example.app.shared_prefs_singleton.utils.Preferences

class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    val database: UserDatabaseClient by lazy {
        UserDatabaseClientImpl(UserDatabase.getInstance(context))
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