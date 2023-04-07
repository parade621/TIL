package com.example.app.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Telephony
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import com.example.app.Utils.DataStoreManager.Keys.MYLOCATION
import com.example.app.Utils.DataStoreManager.Keys.REMEMBER_ME
import com.example.app.Utils.DataStoreManager.Keys.SHOW_COMPLETE
import com.example.app.Utils.DataStoreManager.Keys.SORT_ORDER_KEY
import com.example.app.Utils.DataStoreManager.Keys.USERID
import com.example.app.Utils.DataStoreManager.Keys.USERPROFILE
import com.example.app.Utils.DataStoreManager.Keys.USERPW
import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException


private val Context._dataStore by preferencesDataStore(
    name = Telephony.Mms.Part.FILENAME,
    produceMigrations = { context -> listOf(SharedPreferencesMigration(context, Telephony.Mms.Part.FILENAME)) }
)

@SuppressLint("StaticFieldLeak")
object DataStoreManager {

    private lateinit var dataStore: DataStore<Preferences>

    var datas: Flow<Preferences>? = null
        private set

    fun init(context: Context) {
        dataStore = context._dataStore
        datas = dataStore.data
    }

    private object Keys {
        const val TAG = "DataStoreUtils"
        const val USERID = "userId"
        const val USERPW = "userPw"
        const val USERPROFILE = "userProfile"
        const val REMEMBER_ME = "remember_me"
        const val MYLOCATION = "my_location"
        val SORT_ORDER_KEY = "sort_order"
        val SHOW_COMPLETE = "show_completed"

    }

    val userId: String
        get() = readStringData(USERID)
    val userPw: String
        get() = readStringData(USERPW)
    val userProfile: Int
        get() = readIntData(USERPROFILE)
    val rememberMe: Boolean
        get() = readBooleanData(REMEMBER_ME)
    val location: MutableLiveData<String>
        get() = MutableLiveData<String>(readStringData(MYLOCATION))


    private fun readBooleanData(key: String, default: Boolean = false): Boolean {
        var value = false
        runBlocking {
            dataStore.data.first {
                value = it[booleanPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    private fun readIntData(key: String, default: Int = 0): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[intPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    private fun readStringData(key: String, default: String = ""): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[stringPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    private fun saveStringData(key: String, value: String) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    private fun saveBooleanData(key: String, value: Boolean) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    private fun saveIntData(key: String, value: Int) = runBlocking {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    suspend fun enableSortByDeadline(sortOder: String, enable: Boolean) {
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

    fun logout() {
        saveStringData(USERPW, "")
    }

    fun clearUserInfo() {
        saveStringData(USERID, "")
        saveStringData(USERPW, "")
        saveIntData(USERPROFILE, 0)
    }

    fun setRememberMe() {
        saveBooleanData(REMEMBER_ME, true)
    }

    fun resetRememberMe() {
        saveBooleanData(REMEMBER_ME, false)
    }

    fun setUserProfile(profile: Int) {
        saveIntData(USERPROFILE, profile)
    }

    fun setUserInfo(userId: String, userPw: String, userProfile: Int) {
        saveStringData(USERID, userId)
        saveStringData(USERPW, userPw)
        saveIntData(USERPROFILE, userProfile)
    }

    fun setMyLocation(location: String){
        saveStringData(MYLOCATION, location)
    }

    suspend fun fetchInitialPreferences(): UserPreferences {
        return mapUserPreferences(dataStore.data.first().toPreferences())
    }

    suspend fun enableSortByDeadline(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[stringPreferencesKey(SORT_ORDER_KEY)] ?: SortOrder.NONE.name
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

            preferences[stringPreferencesKey(SORT_ORDER_KEY)] = newSortOrder.name
        }
    }

    suspend fun enableSortByPriority(enable: Boolean) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[stringPreferencesKey(SORT_ORDER_KEY)] ?: SortOrder.NONE.name
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
            preferences[stringPreferencesKey(SORT_ORDER_KEY)] = newSortOrder.name
        }
    }

    suspend fun showCompletedTasks(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(SHOW_COMPLETE)] = showCompleted
        }
    }

    fun userPreferencesFlow(): Flow<UserPreferences> {
        return if (::dataStore.isInitialized) {
            dataStore.data
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
        } else {
            flow {
                emit(UserPreferences(false, SortOrder.NONE))
            }
        }
    }

    fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val sortOrder =
            SortOrder.valueOf(
                preferences[stringPreferencesKey(SORT_ORDER_KEY)] ?: SortOrder.NONE.name
            )

        val showCompleted = preferences[booleanPreferencesKey(SHOW_COMPLETE)] ?: false
        return UserPreferences(showCompleted, sortOrder)
    }
}