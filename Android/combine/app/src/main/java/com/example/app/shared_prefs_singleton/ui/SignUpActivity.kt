package com.example.app.shared_prefs_singleton.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.databinding.ActivitySignUpBinding
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.UserInfo
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    /**
     * 유저 데이터를 Room Database에 insert.
     */

    private val binding : ActivitySignUpBinding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val userDatabaseClient  : UserDatabaseClientImpl by lazy {
        UserDatabaseClientImpl(UserDatabase.getInstance(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupBtn.setOnClickListener {
            if(binding.inputId.text.isNullOrEmpty()){
                Toast.makeText(this@SignUpActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputId.requestFocus()
            }else if(binding.inputPw.text.isNullOrEmpty()){
                Toast.makeText(this@SignUpActivity, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPw.requestFocus()
            }else if(binding.inputPwCheck.text.isNullOrEmpty()){
                Toast.makeText(this@SignUpActivity, "비밀번호 확인란을 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPwCheck.requestFocus()
            }else if(binding.inputPw.text.toString() != binding.inputPwCheck.text.toString()){
                Toast.makeText(this@SignUpActivity, "비밀번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.inputPwCheck.setText("")
                binding.inputPwCheck.requestFocus()
            }
            else{
                val userId =binding.inputId.text.toString()
                val userPw = binding.inputPwCheck.text.toString()
                lifecycleScope.launch {
                    userDatabaseClient.insertUserData(UserInfo(userId, userPw,R.drawable.blue_profile))
                }
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
                finish()
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