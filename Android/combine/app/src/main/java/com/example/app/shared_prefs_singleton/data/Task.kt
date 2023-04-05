package com.example.app.shared_prefs_singleton.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.*

@Parcelize
@Serializable
data class Task(
    var title: String,
    var deadline: Long = Date().time,
    var content: String? = null,
    var priority: String = TaskPriority.LOW.name,
    var completed: Boolean = false
): Parcelable


