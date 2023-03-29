package com.example.app.shared_prefs_singleton.db.client

import com.example.app.shared_prefs_singleton.db.UserInfo

interface UserDatabaseClient {

    suspend fun insertUserData(query: UserInfo)

    suspend fun getAll(): List<UserInfo>

    suspend fun exists(id: String) : Boolean

    suspend fun getUserById(id: String) : UserInfo?

    suspend fun updateProfile(id: String, newProfile : Int)
}