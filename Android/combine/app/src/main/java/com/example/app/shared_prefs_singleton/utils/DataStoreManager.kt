package com.example.app.shared_prefs_singleton.utils

import android.content.Context
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.UserPreferences
import com.example.app.shared_prefs_singleton.utils.DataStoreManager.Keys.PREFS_USER_ID
import com.example.app.shared_prefs_singleton.utils.DataStoreManager.Keys.PREFS_USER_PW
import com.example.app.shared_prefs_singleton.utils.DataStoreManager.Keys.REMEMBER_USER
import com.example.app.shared_prefs_singleton.utils.DataStoreManager.Keys.SHOW_COMPLETE
import com.example.app.shared_prefs_singleton.utils.DataStoreManager.Keys.SORT_ORDER_KEY
import com.example.app.shared_prefs_singleton.utils.DataStoreManager.Keys.USER_PROFILE
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException


private val Context._dataStore by preferencesDataStore(
    name = FILENAME,
    produceMigrations = { context -> listOf(SharedPreferencesMigration(context, FILENAME)) }
)

object DataStoreManager{

    private lateinit var dataStore: DataStore<Preferences>

    fun init(context: Context):DataStoreManager{
        dataStore = context._dataStore
        return this
    }

    // https://developer.android.com/codelabs/android-preferences-datastore?hl=ko#6
    private object Keys{
        val USER_PROFILE = intPreferencesKey("userProfile")
        val PREFS_USER_ID = stringPreferencesKey("userId")
        val PREFS_USER_PW = stringPreferencesKey("userPw")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val SHOW_COMPLETE = booleanPreferencesKey("show_completed")
        val REMEMBER_USER = booleanPreferencesKey("rememberMe")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getData(key:String, default: T): Flow<T>{
        val res = when(default){
            is Long -> readLongData(key, default)
            is String -> readStringData(key, default)
            is Boolean -> readBooleanData(key, default)
            is Int -> readIntData(key, default)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return res as Flow<T>
    }

    suspend fun <T> putData(key:String, value: T){
        when(value){
            is Long -> saveLongData(key,value)
            is String -> saveStringData(key,value)
            is Boolean -> saveBooleanData(key,value)
            is Int -> saveIntData(key,value)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    fun readLongData(key: String, default: Long = 0L): Long {
        var value = 0L
        runBlocking {
            dataStore.data.first {
                value = it[longPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readBooleanData(key:String, default: Boolean = false): Boolean{
        var value = false
        runBlocking {
            dataStore.data.first{
                value = it[booleanPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readIntData(key: String, default: Int = 0): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[intPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readStringData(key: String, default: String = ""): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[stringPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun saveLongData(key: String, value: Long)=runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[longPreferencesKey(key)] = value
            }
        }

    fun saveStringData(key: String, value: String)= runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[stringPreferencesKey(key)] = value
            }
        }
    fun saveBooleanData(key: String, value: Boolean)=runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[booleanPreferencesKey(key)] = value
            }
        }


    fun saveIntData(key: String, value: Int)=runBlocking {
            dataStore.edit { mutablePreferences ->
                mutablePreferences[intPreferencesKey(key)] = value
            }
        }


    // dataStore가 Flow를 사용하기 때문에 우선 아래와 같이 작성하였습니다.
    var userId: String
        get() = runBlocking {
            dataStore.data.first()[PREFS_USER_ID] ?: ""
        }
        set(value) {
            runBlocking {
                dataStore.edit { preferences ->
                    preferences[PREFS_USER_ID] = value
                }
            }
        }

    var userPw: String
        get() = runBlocking {
            dataStore.data.first()[PREFS_USER_PW] ?: ""
        }
        set(value) {
            runBlocking {
                dataStore.edit { preferences ->
                    preferences[PREFS_USER_PW] = value
                }
            }
        }
    var rememberMe: Boolean
        get() = runBlocking {
            dataStore.data.first()[REMEMBER_USER] ?: false
        }
        set(value) {
            runBlocking {
                dataStore.edit { preferences ->
                    preferences[REMEMBER_USER] = value
                }
            }
        }

    var userProfile: Int
        get() = runBlocking {
            dataStore.data.first()[USER_PROFILE] ?: 0
        }
        set(value) {
            runBlocking {
                dataStore.edit { preferences ->
                    preferences[USER_PROFILE] = value
                }
            }
        }

    suspend fun enableSortByDeadline(sortOder:String, enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[stringPreferencesKey(sortOder)] ?: SortOrder.NONE.name
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

            preferences[stringPreferencesKey(sortOder)] = newSortOrder.name
        }
    }


   fun dataStoreDatas(): Flow<Preferences>?{
       return if(this::dataStore.isInitialized) {
           dataStore.data
       }else{
           null
       }
    }

    suspend fun enableSortByPriority(sortOder:String, enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[stringPreferencesKey(sortOder)] ?: SortOrder.NONE.name
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
            preferences[stringPreferencesKey(sortOder)] = newSortOrder.name
        }
    }

    suspend fun showCompletedTasks(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_COMPLETE] = showCompleted
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val sortOrder =
            SortOrder.valueOf(
                preferences[SORT_ORDER_KEY] ?: SortOrder.NONE.name
            )

        val showCompleted = preferences[SHOW_COMPLETE] ?: false
        return UserPreferences(showCompleted, sortOrder)
    }

    fun clearSync() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}