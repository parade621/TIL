package com.example.app.shared_prefs_singleton.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


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