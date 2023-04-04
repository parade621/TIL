package com.example.app.shared_prefs_singleton.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.app.shared_prefs_singleton.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

object DataStoreUtils {
    private const val TAG = "DataStoreUtil"
    private const val USERID = "userId"
    private const val USERPW = "userPw"
    private const val USERPROFILE = "userProfile"
    private const val REMEMBER_ME = "remember_me"
    private const val SORT_ORDER_KEY = "sort_order"
    private const val SHOW_COMPLETE = "show_completed"
    private lateinit var _dataStore: DataStoreManager

    var context: Context? = null
        private set
    var datas: Flow<Preferences>? = null
        private set

    fun init(c: Context?){
        if(c == null){
            Log.e(TAG, "Initialize Context is null")
            return
        }
        context = c
        context?.apply{
            _dataStore = DataStoreManager.init(this)
        }
        datas = _dataStore.dataStoreDatas()
    }


    fun isRememeberMe() : Boolean{
        return if(this::_dataStore.isInitialized){
            _dataStore.readBooleanData(REMEMBER_ME)
        }else{
            false
        }
    }

    fun logout(){
        _dataStore.saveStringData(USERPW,"")
    }
    fun clearUserInfo(){
        _dataStore.saveStringData(USERID,"")
        _dataStore.saveStringData(USERPW,"")
        _dataStore.saveIntData(USERPROFILE, 0)
    }

    fun setRememberMe(){
        _dataStore.saveBooleanData(REMEMBER_ME, true)
    }

    fun resetRememberMe(){
        _dataStore.saveBooleanData(REMEMBER_ME, false)
    }

    fun setUserProfile(profile: Int){
        _dataStore.saveIntData(USERPROFILE, profile)
    }

    fun setUserInfo(userId: String, userPw: String, userProfile:Int){
        _dataStore.saveStringData(USERID,userId)
        _dataStore.saveStringData(USERPW,userPw)
        _dataStore.saveIntData(USERPROFILE, userProfile)
    }

    suspend fun fetchInitialPreferences() =
        _dataStore.mapUserPreferences(datas!!.first().toPreferences())

    suspend fun setEnableSortByDeadline(enable: Boolean){
        _dataStore.enableSortByDeadline(SORT_ORDER_KEY, enable)
    }
    suspend fun setEnableSortByPriority(enable: Boolean){
        _dataStore.enableSortByPriority(SORT_ORDER_KEY, enable)
    }
    suspend fun setShowCompletedTasks(enable: Boolean){
        _dataStore.enableSortByPriority(SORT_ORDER_KEY, enable)
    }




    val userId: String
        get() = _dataStore.readStringData(USERID)
    val userPw: String
        get() = _dataStore.readStringData(USERPW)
    val userProfile: Int
        get() = _dataStore.readIntData(USERPROFILE)
    val rememberMe: Boolean
        get() = _dataStore.readBooleanData(REMEMBER_ME)

    val userPreferencesFlow: Flow<UserPreferences> = DataStoreManager.dataStoreDatas()!!
        .catch { exception ->
            if (exception is IOException) {
                Log.e("DataStoreError", "Error from IO", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            _dataStore.mapUserPreferences(preferences)
        }

}