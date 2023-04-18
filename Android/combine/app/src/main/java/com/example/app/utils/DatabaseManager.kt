package com.example.app.Utils

import android.content.Context
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.shared_prefs_singleton.data.UserInfo
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.utils.DataStoreManager

object DatabaseManager {

    lateinit var database: UserDatabase
        private set

    fun init(context: Context) {
        database = UserDatabase.getInstance(context)
    }

    suspend fun insertUserData(query: UserInfo) {
        database.userInfoDao().insertUserData(query)
    }

    suspend fun getAll(): List<UserInfo> {
        return database.userInfoDao().getAll()
    }

    suspend fun exists(id: String): Boolean {
        return database.userInfoDao().exists(id)
    }

    suspend fun getUserById(id: String): UserInfo? {
        return database.userInfoDao().getUserById(id)
    }

    suspend fun updateProfile(id: String, newProfile: Int) {
        val user = database.userInfoDao().getUserById(id)
        if (user != null) {
            user.userProfile = newProfile
            database.userInfoDao().updateUser(user)
        }
    }

    suspend fun updateTask() {
        val user = database.userInfoDao().getUserById(DataStoreManager.userId)!!
        user.userTask = TasksRepository.getAllTask()
        database.userInfoDao().insertUserData(user)
    }
}