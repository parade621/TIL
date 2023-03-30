package com.example.app.shared_prefs_singleton.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [UserInfo::class],
    version = 1,
    exportSchema = false
)

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
                .addMigrations(MIGRATION_2_1)
                .build()

        fun getInstance(context: Context): UserDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it }
            }

        val MIGRATION_2_1 = object : Migration(2, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE table_name ADD COLUMN new_column_name TEXT")
            }
        }

    }
}