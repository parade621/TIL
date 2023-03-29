package com.example.app.shared_prefs_singleton.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfo (
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId : String,
    val userPw: String,
    var userProfile: Int
)