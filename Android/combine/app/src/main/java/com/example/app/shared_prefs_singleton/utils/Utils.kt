package com.example.app.shared_prefs_singleton.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun hideKeyboardOnTouchOutside(activity: Activity, event: MotionEvent?) {
    if (event?.action == MotionEvent.ACTION_DOWN) {
        val view = activity.currentFocus
        if (view is EditText) {
            val outRect = Rect().apply { view.getGlobalVisibleRect(this) }
            if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                view.clearFocus()
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}
