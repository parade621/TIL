package com.example.app.shared_prefs_singleton.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.app.shared_prefs_singleton.db.converter.TaskPriorityConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "books")
@TypeConverters(TaskPriorityConverter::class)
data class Task(
    @PrimaryKey(autoGenerate = false)
    @field:Json(name = "title")
    val title : String,
    @field:Json(name = "deadline")
    val deadline: Long,
    @field:Json(name = "content")
    val content: String?= null,
    @field:Json(name = "priority")
    val priority: TaskPriority, // 이게 지금 Moshi 컨버터에서 변환이 안되는 값이라서 안됨. 이걸 어떻게 해결해야하는데...
    @field:Json(name = "completed")
    val completed: Boolean = false
): Parcelable

