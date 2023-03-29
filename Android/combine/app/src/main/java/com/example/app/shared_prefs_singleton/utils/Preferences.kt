package com.example.app.shared_prefs_singleton.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private const val FILENAME = "com.example.app.shared_prefs_singleton.utils.my_preference"
    private lateinit var _preferences: SharedPreferences
    val preferences get() = _preferences

    fun init(context: Context) {
        _preferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE)
    }

    private val USER_PROFILE = "userProfile"
    private val REMEMBER_USER = "rememberMe"
    private val PREFS_USER_ID = "userId"
    private val PREFS_USER_PW = "userPw"


    var userId: String
        get() = _preferences.getString(PREFS_USER_ID, "").toString()
        set(value) = _preferences.edit().putString(PREFS_USER_ID, value).apply()

    var userPw: String
        get() = _preferences.getString(PREFS_USER_PW, "").toString()
        set(value) = _preferences.edit().putString(PREFS_USER_PW, value).apply()

    var rememberMe: Boolean
        get() = _preferences.getBoolean(REMEMBER_USER, false)
        set(value) = _preferences.edit().putBoolean(REMEMBER_USER, value).apply()

    var userProfile: Int
        get() = _preferences.getInt(USER_PROFILE, 0)
        set(value) = _preferences.edit().putInt(USER_PROFILE, value).apply()

}