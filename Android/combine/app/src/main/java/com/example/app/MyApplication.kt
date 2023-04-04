package com.example.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClient
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl
import com.example.app.shared_prefs_singleton.utils.MyPreferences
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule

class MyApplication : Application() {

    private lateinit var myDataStore: PreferenceDataStoreModule
    private lateinit var myDataBase:UserDatabaseClientImpl
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        private lateinit var myApplication: MyApplication
        fun getInstance() = myApplication
    }

    @Override
    override fun onCreate() {
        super.onCreate()
        MyPreferences.init(this)
        myDataStore = PreferenceDataStoreModule(this@MyApplication) // 싱글톤으로 생성
        myDataBase = UserDatabaseClientImpl(UserDatabase.getInstance(this@MyApplication))
        myApplication = this@MyApplication
        context = applicationContext
    }

    fun getDataStore(): PreferenceDataStoreModule = myDataStore
    fun getDataBase() = myDataBase

    @Override
    override fun attachBaseContext(base: Context) {
        MyPreferences.init(base)
        super.attachBaseContext(base)
    }
}