package com.example.app.shared_prefs_singleton.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.MyApplication
import com.example.app.databinding.ActivityLogInBinding
import com.example.app.shared_prefs_singleton.utils.Preferences
import com.example.app.shared_prefs_singleton.utils.hideKeyboardOnTouchOutside
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity : AppCompatActivity() {
    private val binding: ActivityLogInBinding by lazy {
        ActivityLogInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Preferences.rememberMe) {
            binding.rememberMe.isChecked = true
        }
        if (Preferences.userId.isNotEmpty()) {
            binding.inputId.setText(Preferences.userId)
            binding.inputPw.requestFocus()
        }

        binding.rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 현재 체크되어있음
                if (Preferences.rememberMe) {
                    // 이미 체크된 상태면 체크 해제
                    binding.rememberMe.isChecked = false
                    Preferences.rememberMe = false
                } else {
                    // 체크 안되있으면 체크
                    Preferences.rememberMe = isChecked
                }
            } else {
                // 체크 안되어있으면 false
                Preferences.rememberMe = false
            }
        }

        binding.signinBtn.setOnClickListener {
            val userId = binding.inputId.text.toString()
            val userPw = binding.inputPw.text.toString()

            if (userId.isNullOrEmpty()) {
                Toast.makeText(this@LogInActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputId.requestFocus()
            }
            if (userPw.isNullOrEmpty()) {
                Toast.makeText(this@LogInActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPw.requestFocus()
            }

            lifecycleScope.launch(Dispatchers.Default) {
                if (!(application as MyApplication).database.exists(userId)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LogInActivity, "유효하지 않은 아이디입니다.", Toast.LENGTH_SHORT)
                            .show()
                        binding.inputId.setText("")
                        binding.inputPw.setText("")
                        binding.inputId.requestFocus()
                    }
                } else {
                    val userInfo = (application as MyApplication).database.getUserById(userId)!!
                    if (userInfo.userPw != userPw) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LogInActivity,
                                "비밀번호가 옳바르지 않습니다.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            binding.inputPw.setText("")
                            binding.inputPw.requestFocus()
                        }
                    } else {
                        Preferences.userId = userId
                        Preferences.userPw = userPw
                        Preferences.userProfile = userInfo.userProfile
                        startUserDataActivity(this@LogInActivity)
                    }
                }
            }
        }

        binding.signupTextLayout.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun startUserDataActivity(context: Context) = withContext(Dispatchers.Main) {
        val intent = Intent(context, TasksActivity::class.java)
        context.startActivity(intent)
        finish()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        hideKeyboardOnTouchOutside(this, event)
        return super.dispatchTouchEvent(event)
    }
}

