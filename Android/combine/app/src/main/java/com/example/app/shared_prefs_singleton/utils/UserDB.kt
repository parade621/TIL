package com.example.app.shared_prefs_singleton.utils

import com.example.app.MyApplication
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl

object UserDB {
    val db = UserDatabaseClientImpl(UserDatabase.getInstance(MyApplication.context))
}