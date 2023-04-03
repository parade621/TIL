package com.example.app.shared_prefs_singleton.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.app.shared_prefs_singleton.data.Task
import com.example.app.shared_prefs_singleton.data.TaskPriority
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListConverters {
    @TypeConverter
    fun fromList(value: List<Task>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<Task>>(value)
}
@ProvidedTypeConverter
class TaskPriorityConverter(private val moshi: Moshi.Builder){
    private val taskPriorityAdapter: JsonAdapter<TaskPriority> =
        moshi.build().adapter(TaskPriority::class.java)
    @FromJson
    fun fromJson(priority: String): TaskPriority{
        return when(priority){
            "HIGH" -> TaskPriority.HIGH
            "MEDIUM" -> TaskPriority.MEDIUM
            "LOW" -> TaskPriority.LOW
            else -> throw IllegalAccessException("Unknown priority: $priority")
        }
    }

    @ToJson
    fun toJson(priority: TaskPriority): String{
        return priority.name
    }
}