package com.example.webview_js

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.webview_js.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val webView: WebView = binding.webview
        webView.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JavascriptBridge(this@MainActivity), "Android")
            loadUrl("file:///android_asset/sample.html")
            webViewClient = WebViewClient()
        }
    }
}