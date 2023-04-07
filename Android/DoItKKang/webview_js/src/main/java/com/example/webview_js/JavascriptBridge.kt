package com.example.webview_js

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class JavascriptBridge(val context: Context){
    @JavascriptInterface
    fun showToast(toast:String){
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }
}