package com.example.app.webview1

import android.os.Bundle
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityWebView1Binding

class WebView1Activity : AppCompatActivity() {

    private val binding: ActivityWebView1Binding by lazy{
        ActivityWebView1Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val webview = binding.webview.apply{
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebViewJavascriptBridge(), "Android")
            loadUrl("file:///android_asset/exam.html")
        }

        binding.doBtn.setOnClickListener {
            webview.loadUrl("javascript:exam_script.plus_num(" + binding.inputNumber.text + ")")
        }

    }

    inner class WebViewJavascriptBridge{
        @JavascriptInterface
        fun getDoubleNum(num: Int){
            binding.result.text = num.toString()
        }
    }
}

