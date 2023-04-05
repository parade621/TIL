package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.MyApplication
import com.example.app.databinding.ActivityLogInBinding
import com.example.app.shared_prefs_singleton.utils.DataStoreManager
import com.example.app.shared_prefs_singleton.utils.hideKeyboardOnTouchOutside
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInActivity : AppCompatActivity() {
    private val binding: ActivityLogInBinding by lazy {
        ActivityLogInBinding.inflate(layoutInflater)
    }

    private val dataBase = MyApplication.getInstance().getDataBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (DataStoreManager.rememberMe) {
            binding.rememberMe.isChecked = true
        }

        if (DataStoreManager.userId.isNotEmpty()) {
            binding.inputId.setText(DataStoreManager.userId)
            binding.inputPw.requestFocus()
        }

        binding.rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 현재 체크되어있음
                if (DataStoreManager.rememberMe) {
                    // 이미 체크된 상태면 체크 해제
                    binding.rememberMe.isChecked = false
                    DataStoreManager.resetRememberMe()
                } else {
                    // 체크 안되있으면 체크
                    DataStoreManager.setRememberMe()
                }
            } else {
                // 체크 안되어있으면 false
                DataStoreManager.resetRememberMe()
            }
        }

        binding.signinBtn.setOnClickListener {
            val inputUserId = binding.inputId.text.toString()
            val inputUserPw = binding.inputPw.text.toString()

            if (inputUserId.isNullOrEmpty()) {
                Toast.makeText(this@LogInActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputId.requestFocus()
            }
            if (inputUserPw.isNullOrEmpty()) {
                Toast.makeText(this@LogInActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPw.requestFocus()
            }
            lifecycleScope.launch(Dispatchers.Default) {
                if (!dataBase.exists(inputUserId)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LogInActivity,
                            "유효하지 않은 아이디입니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.inputId.setText("")
                        binding.inputPw.setText("")
                        binding.inputId.requestFocus()
                    }
                } else {
                    val userInfo = dataBase.getUserById(inputUserId)!!
                    if (userInfo.userPw != inputUserPw) {
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
                        DataStoreManager.setUserInfo(inputUserId,inputUserPw,userInfo.userProfile)
                        val intent = Intent(this@LogInActivity, TasksActivity::class.java)
                        this@LogInActivity.startActivity(intent)
                        finish()
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

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        hideKeyboardOnTouchOutside(this, event)
        return super.dispatchTouchEvent(event)
    }
}

