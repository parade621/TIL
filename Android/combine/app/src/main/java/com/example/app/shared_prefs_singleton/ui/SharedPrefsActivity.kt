package com.example.app.shared_prefs_singleton.ui

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.databinding.ActivitySharedPrefsBinding
import com.example.app.shared_prefs_singleton.utils.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedPrefsActivity : AppCompatActivity() {

    // splash activity 역할을 수행할 예정.

    private val binding: ActivitySharedPrefsBinding by lazy {
        ActivitySharedPrefsBinding.inflate(layoutInflater)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Preferences.init(this)
        context = applicationContext

        lifecycleScope.launch {
            delay(1000L)
            if (Preferences.rememberMe == true) {
                startUserDataActivity(this@SharedPrefsActivity)
            } else {
                toLogInActivity(this@SharedPrefsActivity)
            }
        }
    }

    private suspend fun startUserDataActivity(context: Context) = withContext(Dispatchers.Main) {
        val intent = Intent(context, UserDataActivity::class.java)
        context.startActivity(intent)
    }

    private suspend fun toLogInActivity(context: Context) = withContext(Dispatchers.Main) {
        val intent = Intent(context, LogInActivity::class.java)
        context.startActivity(intent)
    }

    override fun attachBaseContext(base: Context) {
        Preferences.init(base)
        super.attachBaseContext(base)
    }
}