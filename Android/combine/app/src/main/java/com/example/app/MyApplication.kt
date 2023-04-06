package com.example.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.app.Utils.DatabaseManager
import com.example.app.Utils.DataStoreManager
import com.example.app.Utils.MyPreferences

class MyApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        private lateinit var myApplication: MyApplication
        fun getInstance() = myApplication
    }

    @Override
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        myApplication = this@MyApplication
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