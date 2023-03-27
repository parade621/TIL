package com.example.app.camera_view.util

import android.view.View

class OnSingleClickListener(private val onSingleClick: (View) -> Unit) : View.OnClickListener {
    // 커스텀 클릭 리스너를 사용하는 이유를 질문( 특별한 이유가 있을거 같아서 알고싶다. )
    companion object {
        const val CLICK_INTERVAL = 500
    }

    private var lastClickedTime: Long = 0L

    private fun isSafe(): Boolean {
        return System.currentTimeMillis() - lastClickedTime > CLICK_INTERVAL
    }

    override fun onClick(view: View?) {
        if (isSafe() && view != null) {
            onSingleClick(view)
        }
        lastClickedTime = System.currentTimeMillis()
    }
}

fun View.setOnSingleClickListener(block: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener {
        block(it)
    })
}