package com.example.app.shared_prefs_singleton.db

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.app.shared_prefs_singleton.db.converter.ListConverters
import com.example.app.shared_prefs_singleton.db.converter.TaskPriorityConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


@Database(
    entities = [UserInfo::class],
    version = 1,
    exportSchema = false
)

//@TypeConverters(ListConverters::class, TaskPriorityConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private fun buildDatabase(context: Context): UserDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "user_info"
            )
                .build()

        fun getInstance(context: Context): UserDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it }
            }

    }
}