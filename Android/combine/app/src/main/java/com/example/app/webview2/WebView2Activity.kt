package com.example.app.webview2

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityWebView2Binding
import org.json.JSONObject

class WebView2Activity : AppCompatActivity() {

    private val binding: ActivityWebView2Binding by lazy {
        ActivityWebView2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var userAgent: String? = null

        binding.ivBack.setOnClickListener {
            if (binding.webview.canGoBack()) {
                binding.webview.goBack()
            }
        }
        binding.btnWebviewLocal.setOnClickListener {
            binding.webview.loadUrl("file:///android_asset/index.html")
        }
        binding.btnJsCall.setOnClickListener {
            binding.webview.loadUrl("javascript:nativeCallFunc('웹뷰 통신 테스트')")
        }
        binding.btnWebview.setOnClickListener {
            binding.webview.loadUrl("https://www.google.com")
            Log.d("일함", "되는 중")
        }


        // 웹뷰 설정
        binding.webview.apply {
            settings.apply {
                settings.builtInZoomControls = false
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    displayZoomControls = false
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    textZoom = 100
                }
                javaScriptEnabled = true
            }

            // JavaScript interface 추가
            addJavascriptInterface(AndroidBridge(), "Android")

            // 웹뷰 페이지 로딩 시작/종료 이벤트에 토스트 메시지 나오게
                webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Toast.makeText(this@WebView2Activity, "OnPageStared", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Toast.makeText(this@WebView2Activity, "onPageFinished", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // 웹뷰 자바스크립트 얼럿, 컨펌
            webChromeClient = object : WebChromeClient() {
                override fun onJsAlert(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    if (message?.indexOf("\"action\"")!! >= 0) {
                        try {
                            val jsonObject = JSONObject(message)
                            val action = jsonObject.get("action").toString()
                            val msg = jsonObject.get("msg").toString()
                            AlertDialog.Builder(this@WebView2Activity)
                                .setTitle("알림")
                                .setMessage(msg)
                                .setPositiveButton(
                                    android.R.string.ok,
                                    DialogInterface.OnClickListener { dialogInterface, i ->
                                        result?.confirm()
                                        if ("finish".equals(action)) {
                                            finish()
                                        }
                                    }
                                ).setCancelable(false).create().show()
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                        }
                        // 아래를 호출해줘야 웹의 블럭이 풀린다.
                        result?.confirm()
                    } else {
                        AlertDialog.Builder(this@WebView2Activity)
                            .setTitle("알림")
                            .setMessage(message)
                            .setPositiveButton(
                                android.R.string.ok,
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    result?.confirm()
                                }
                            ).setCancelable(false).create().show()
                    }
                    return true
                }

                override fun onJsConfirm(
                    view: WebView?,
                    url: String?,
                    message: String?,
                    result: JsResult?
                ): Boolean {
                    AlertDialog.Builder(this@WebView2Activity)
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(
                            android.R.string.ok,
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                result?.confirm()
                            }
                        ).setCancelable(false).create().show()

                    return true
                }
            }

            userAgent = settings.userAgentString
            settings.userAgentString = userAgent + " Android_Mobile"

        }

    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun bridgeNativeCall(string: String) {
            Toast.makeText(this@WebView2Activity, "bridgeNativeCall : $string", Toast.LENGTH_SHORT)
                .show()
        }
    }
}