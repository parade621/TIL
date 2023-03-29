package com.example.app.shared_prefs_singleton.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val FILENAME = "com.example.app.shared_prefs_singleton.utils.my_preference"
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE)
    }

    private val USER_PROFILE = "userProfile"
    private val REMEMBER_USER = "rememberMe"
    private val PREFS_USER_ID = "userId"
    private val PREFS_USER_PW = "userPw"


    var userId: String
        get() = preferences.getString(PREFS_USER_ID, "").toString()
        set(value) = preferences.edit().putString(PREFS_USER_ID, value).apply()

    var userPw: String
        get() = preferences.getString(PREFS_USER_PW, "").toString()
        set(value) = preferences.edit().putString(PREFS_USER_PW, value).apply()

    var rememberMe: Boolean
        get() = preferences.getBoolean(REMEMBER_USER, false)
        set(value) = preferences.edit().putBoolean(REMEMBER_USER, value).apply()

    var userProfile: Int
        get() = preferences.getInt(USER_PROFILE, 0)
        set(value) = preferences.edit().putInt(USER_PROFILE, value).apply()

}