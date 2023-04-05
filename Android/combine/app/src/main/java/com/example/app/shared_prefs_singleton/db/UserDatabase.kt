package com.example.app.shared_prefs_singleton.db

import android.content.Context
import androidx.room.*
import com.example.app.shared_prefs_singleton.data.UserInfo
import com.example.app.shared_prefs_singleton.db.converter.ListConverters


@Database(
    entities = [UserInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListConverters::class)
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