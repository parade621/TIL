package com.example.app.shared_prefs_singleton.db

import androidx.room.*

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(query: UserInfo)

    @Query("SELECT * FROM user_info")
    suspend fun getAll(): List<UserInfo>

    @Query("SELECT EXISTS(SELECT 1 FROM user_info WHERE userId = :id LIMIT 1)")
    suspend fun exists(id: String): Boolean

    @Query("SELECT * FROM user_info WHERE userId = :id")
    fun getUserById(id: String): UserInfo?

    @Update
    suspend fun updateUser(userinfo: UserInfo)
}