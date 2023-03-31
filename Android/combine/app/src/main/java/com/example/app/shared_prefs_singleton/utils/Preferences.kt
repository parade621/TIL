package com.example.app.shared_prefs_singleton.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.app.shared_prefs_singleton.data.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object Preferences {
    private const val FILENAME = "com.example.app.shared_prefs_singleton.utils.my_preference"
    private lateinit var _preferences: SharedPreferences
    private lateinit var _sortOrderFlow: MutableStateFlow<SortOrder>
    private lateinit var _showCompleted: MutableStateFlow<Boolean>
    val sortOrderFlow: StateFlow<SortOrder> by lazy {
        _sortOrderFlow
    }
    val showCompleted: StateFlow<Boolean> by lazy {
        _showCompleted
    }

    val preferences get() = _preferences

    fun init(context: Context) {
        _preferences = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE)
        _sortOrderFlow = MutableStateFlow(sortOrder)
        _showCompleted = MutableStateFlow(showComplete)
    }

    private val USER_PROFILE = "userProfile"
    private val REMEMBER_USER = "rememberMe"
    private val PREFS_USER_ID = "userId"
    private val PREFS_USER_PW = "userPw"
    private val SORT_ORDER_KEY = "sort_order"
    private val SHOW_COMPLETE = "show_completed"

    private val sortOrder: SortOrder
        get() {
            val order = preferences.getString(SORT_ORDER_KEY, SortOrder.NONE.name)
            return SortOrder.valueOf(order ?: SortOrder.NONE.name)
        }
    private val showComplete: Boolean
        get() {
            val order = preferences.getBoolean(SHOW_COMPLETE, false)
            return order
        }

    var userId: String
        get() = _preferences.getString(PREFS_USER_ID, "").toString()
        set(value) = _preferences.edit().putString(PREFS_USER_ID, value).apply()

    var userPw: String
        get() = _preferences.getString(PREFS_USER_PW, "").toString()
        set(value) = _preferences.edit().putString(PREFS_USER_PW, value).apply()

    var rememberMe: Boolean
        get() = _preferences.getBoolean(REMEMBER_USER, false)
        set(value) = _preferences.edit().putBoolean(REMEMBER_USER, value).apply()

    var userProfile: Int
        get() = _preferences.getInt(USER_PROFILE, 0)
        set(value) = _preferences.edit().putInt(USER_PROFILE, value).apply()

    fun showCompletedTasks(enable: Boolean) {
        updateShowState(enable)
        _showCompleted.value = enable
    }


    // 마감일을 기준으로 정렬
    fun enableSortByDeadLine(enable: Boolean) {
        val currentOrder = sortOrderFlow.value
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
        updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }

    fun enableSortByPriority(enable: Boolean) {
        val currentOrder = sortOrderFlow.value
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
        updateSortOrder(newSortOrder)
        _sortOrderFlow.value = newSortOrder
    }

    private fun updateSortOrder(sortOrder: SortOrder) {
        preferences.edit {
            putString(SORT_ORDER_KEY, sortOrder.name)
        }
    }

    private fun updateShowState(newState: Boolean) {
        preferences.edit {
            putBoolean(SHOW_COMPLETE, newState).apply()
        }
    }


}