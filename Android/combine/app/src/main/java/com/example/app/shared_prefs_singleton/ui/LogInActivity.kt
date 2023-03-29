package com.example.app.shared_prefs_singleton.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.service.notification.Condition.isValidId
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.rangeTo
import androidx.lifecycle.lifecycleScope
import com.example.app.databinding.ActivityLogInBinding
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.UserInfo
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClient
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl
import com.example.app.shared_prefs_singleton.utils.Preferences
import com.example.app.shared_prefs_singleton.utils.Preferences.userId
import com.example.app.shared_prefs_singleton.utils.Preferences.userPw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity : AppCompatActivity() {
    private val binding: ActivityLogInBinding by lazy {
        ActivityLogInBinding.inflate(layoutInflater)
    }

    private val userDatabaseClient: UserDatabaseClientImpl by lazy {
        UserDatabaseClientImpl(UserDatabase.getInstance(applicationContext))
    }

    private var userInfo: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var checkBoxChecked = Preferences.rememberMe
        if (checkBoxChecked) {
            binding.rememberMe.isChecked = true
        }
        if(Preferences.userId.isNotEmpty()){
            binding.inputId.setText(Preferences.userId)
            binding.inputPw.requestFocus()
        }

        binding.rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 현재 체크되어있음
                if (checkBoxChecked) {
                    // 이미 체크된 상태면 체크 해제
                    binding.rememberMe.isChecked = false
                    checkBoxChecked = false
                    Preferences.rememberMe = false
                } else {
                    // 체크 안되있으면 체크
                    checkBoxChecked = true
                    Preferences.rememberMe = isChecked
                }
            } else {
                // 체크 안되어있으면 false
                checkBoxChecked = false
                Preferences.rememberMe = false
            }
        }

        binding.signinBtn.setOnClickListener {
            val userId = binding.inputId.text.toString()
            val userPw = binding.inputPw.text.toString()
            lifecycleScope.launch(Dispatchers.Default) {
                if (isValidId(userId) && isValidPw(userPw)) {
                    Preferences.userId = userInfo!!.userId
                    Preferences.userPw = userInfo!!.userPw
                    Preferences.userProfile = userInfo!!.userProfile
                    startUserDataActivity(this@LogInActivity)
                }
            }
        }

        binding.signupTextLayout.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun startUserDataActivity(context: Context) = withContext(Dispatchers.Main){
        val intent = Intent(context, UserDataActivity::class.java)
        context.startActivity(intent)
        finish()
    }

    private suspend fun isValidId(id: String?): Boolean {
        if (id.isNullOrEmpty()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LogInActivity, "유효하지 않은 아이디입니다.", Toast.LENGTH_SHORT).show()
                binding.inputId.requestFocus()
            }
            return false
        } else {
            var result: Boolean? = null
            result = userDatabaseClient.exists(id)
            if (result!!) {
                userInfo = userDatabaseClient.getUserById(id)
                return true
            }else{
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LogInActivity, "유효하지 않은 아이디입니다.", Toast.LENGTH_SHORT).show()
                    binding.inputId.setText("")
                    binding.inputPw.setText("")
                    binding.inputId.requestFocus()
                }
                return false
            }
        }
    }

    private suspend fun isValidPw(pw: String?): Boolean {
        if (pw.isNullOrEmpty()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LogInActivity, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPw.requestFocus()
            }
            return false
        } else {
            if (userInfo?.userPw == pw) {
                return true
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LogInActivity, "비밀번호가 옳바르지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                    binding.inputPw.setText("")
                    binding.inputPw.requestFocus()
                }
                return false
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect().apply { view.getGlobalVisibleRect(this) }
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}

