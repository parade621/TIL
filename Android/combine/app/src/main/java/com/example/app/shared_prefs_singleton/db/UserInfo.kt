package com.example.app.shared_prefs_singleton.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.app.shared_prefs_singleton.data.Task
import com.example.app.shared_prefs_singleton.db.converter.TaskPriorityConverter

@Entity(tableName = "user_info")
data class UserInfo(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    val userId: String,
    val userPw: String,
    var userProfile: Int,
//    var userTask: List<Task>? = null
)