package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.databinding.ActivityUserDataBinding
import com.example.app.shared_prefs_singleton.dialog.ProfileChooseDialog
import com.example.app.shared_prefs_singleton.utils.Preferences
import com.example.app.shared_prefs_singleton.utils.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataActivity : AppCompatActivity() {

    // 로그인 이후에 유저 정보가 보여질 화면.
    private val binding: ActivityUserDataBinding by lazy {
        ActivityUserDataBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Preferences.rememberMe) {
            binding.rememberMe.isChecked = true
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
                    Preferences.rememberMe = true
                }
            } else {
                // 체크 안되어있으면 false
                Preferences.rememberMe = false
            }
        }

        binding.userIdInfo.text =
            String.format(resources.getString(R.string.user_id, Preferences.userId))

        binding.userProfileImage.setOnClickListener {
            showDialog()
        }

        binding.prefList.setOnClickListener {
            val intent = Intent(this@UserDataActivity, PreferenceListActivity::class.java)
            startActivity(intent)
        }

        binding.logOutBtn.setOnClickListener {
            Preferences.userPw = ""
            val intent = Intent(this@UserDataActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.userProfileImage.setImageResource(Preferences.userProfile)
    }

    private fun showDialog() {
        val intent = Intent(this, ProfileChooseDialog::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        Log.d("바꾸기 누름", "true")
    }

}