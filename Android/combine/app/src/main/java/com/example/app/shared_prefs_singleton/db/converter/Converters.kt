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
    fun fromList(value: List<Task>?) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<Task>>(value)
}

