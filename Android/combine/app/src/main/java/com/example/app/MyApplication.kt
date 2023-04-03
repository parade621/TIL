package com.example.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClient
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl
import com.example.app.shared_prefs_singleton.utils.MyPreferences

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

        MyPreferences.init(this)

        context = applicationContext
    }

    @Override
    override fun attachBaseContext(base: Context) {
        MyPreferences.init(base)
        super.attachBaseContext(base)
    }
}