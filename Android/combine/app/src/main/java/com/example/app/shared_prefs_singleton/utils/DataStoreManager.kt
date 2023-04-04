package com.example.app.shared_prefs_singleton.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.UserPreferences
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule.Keys.PREFS_USER_ID
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule.Keys.PREFS_USER_PW
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule.Keys.REMEMBER_USER
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule.Keys.SHOW_COMPLETE
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule.Keys.SORT_ORDER_KEY
import com.example.app.shared_prefs_singleton.utils.PreferenceDataStoreModule.Keys.USER_PROFILE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

class PreferenceDataStoreModule(private val context: Context) {
    private val FILENAME = "com.example.app.shared_prefs_singleton.utils.my_preference"
    private val Context._dataStore by preferencesDataStore(
        name = FILENAME,
        produceMigrations = { context -> listOf(SharedPreferencesMigration(context, FILENAME)) }
    )
    val dateStore get() = context._dataStore

    // https://developer.android.com/codelabs/android-preferences-datastore?hl=ko#6
    private object Keys{
        val USER_PROFILE = intPreferencesKey("userProfile")
        val PREFS_USER_ID = stringPreferencesKey("userId")
        val PREFS_USER_PW = stringPreferencesKey("userPw")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val SHOW_COMPLETE = booleanPreferencesKey("show_completed")
        val REMEMBER_USER = booleanPreferencesKey("rememberMe")
    }

    // dataStore가 Flow를 사용하기 때문에 우선 아래와 같이 작성하였습니다.
    var userId: String
        get() = runBlocking {
            context._dataStore.data.first()[PREFS_USER_ID] ?: ""
        }
        set(value) {
            runBlocking {
                context._dataStore.edit { preferences ->
                    preferences[PREFS_USER_ID] = value
                }
            }
        }

    var userPw: String
        get() = runBlocking {
            context._dataStore.data.first()[PREFS_USER_PW] ?: ""
        }
        set(value) {
            runBlocking {
                context._dataStore.edit { preferences ->
                    preferences[PREFS_USER_PW] = value
                }
            }
        }
    var rememberMe: Boolean
        get() = runBlocking {
            context._dataStore.data.first()[REMEMBER_USER] ?: false
        }
        set(value) {
            runBlocking {
                context._dataStore.edit { preferences ->
                    preferences[REMEMBER_USER] = value
                }
            }
        }

    var userProfile: Int
        get() = runBlocking {
            context._dataStore.data.first()[USER_PROFILE] ?: 0
        }
        set(value) {
            runBlocking {
                context._dataStore.edit { preferences ->
                    preferences[USER_PROFILE] = value
                }
            }
        }

    val userPreferencesFlow: Flow<UserPreferences> = dateStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e("DataStoreError", "Error from IO", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun enableSortByDeadline(enable: Boolean) {
        context._dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.NONE.name
            )

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_PRIORITY) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_DEADLINE
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_PRIORITY
                    } else {
                        SortOrder.NONE
                    }
                }

            preferences[SORT_ORDER_KEY] = newSortOrder.name
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        context._dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.NONE.name
            )

            val newSortOrder =
                if (enable) {
                    if (currentOrder == SortOrder.BY_DEADLINE) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_PRIORITY
                    }
                } else {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_DEADLINE
                    } else {
                        SortOrder.NONE
                    }
                }
            preferences[SORT_ORDER_KEY] = newSortOrder.name
        }
    }

    suspend fun showCompletedTasks(showCompleted: Boolean) {
        context._dataStore.edit { preferences ->
            preferences[SHOW_COMPLETE] = showCompleted
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(context._dataStore.data.first().toPreferences())


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val sortOrder =
            SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.NONE.name
            )

        val showCompleted = preferences[SHOW_COMPLETE] ?: false
        return UserPreferences(showCompleted, sortOrder)
    }
}