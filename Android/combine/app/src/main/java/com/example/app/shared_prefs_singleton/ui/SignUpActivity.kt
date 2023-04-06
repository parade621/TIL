package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.databinding.ActivitySignUpBinding
import com.example.app.shared_prefs_singleton.data.UserInfo
import com.example.app.Utils.DataStoreManager
import com.example.app.Utils.DatabaseManager
import com.example.app.shared_prefs_singleton.utils.hideKeyboardOnTouchOutside
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    /**
     * 유저 데이터를 Room Database에 insert.
     */

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            if (binding.inputId.text.isNullOrEmpty()) {
                Toast.makeText(this@SignUpActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputId.requestFocus()
            } else if (binding.inputPw.text.isNullOrEmpty()) {
                Toast.makeText(this@SignUpActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPw.requestFocus()
            } else if (binding.inputPwCheck.text.isNullOrEmpty()) {
                Toast.makeText(this@SignUpActivity, "비밀번호 확인란을 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPwCheck.requestFocus()
            } else if (binding.inputPw.text.toString() != binding.inputPwCheck.text.toString()) {
                Toast.makeText(this@SignUpActivity, "비밀번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPwCheck.setText("")
                binding.inputPwCheck.requestFocus()
            } else {
                val inputUserId = binding.inputId.text.toString()
                val iputUserPw = binding.inputPwCheck.text.toString()
                lifecycleScope.launch(Dispatchers.IO) {
                    DatabaseManager.insertUserData(
                        UserInfo(
                            inputUserId,
                            iputUserPw,
                            R.drawable.blue_profile,
                            emptyList()
                        )
                    )
                }
                DataStoreManager.clearUserInfo()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        hideKeyboardOnTouchOutside(this, event)
        return super.dispatchTouchEvent(event)
    }
}