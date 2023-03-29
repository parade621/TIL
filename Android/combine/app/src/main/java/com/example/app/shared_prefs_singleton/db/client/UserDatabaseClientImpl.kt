package com.example.app.shared_prefs_singleton.db.client

import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.UserInfo
import com.example.app.shared_prefs_singleton.db.UserInfoDao

class UserDatabaseClientImpl(
    private val db: UserDatabase,
):UserDatabaseClient {
    override suspend fun insertUserData(query: UserInfo) {
        db.userInfoDao().insertUserData(query)
    }
    override suspend fun getAll(): List<UserInfo> {
        return db.userInfoDao().getAll()
    }

    override suspend fun exists(id: String): Boolean {
        return db.userInfoDao().exists(id)
    }

    override suspend fun getUserById(id: String): UserInfo? {
        return db.userInfoDao().getUserById(id)
    }

    override suspend fun updateProfile(id: String, newProfile: Int) {
        val user = db.userInfoDao().getUserById(id)
        if(user!=null){
            user.userProfile = newProfile
            db.userInfoDao().updateUser(user)
        }
    }

}