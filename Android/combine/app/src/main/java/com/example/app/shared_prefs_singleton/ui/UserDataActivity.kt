package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.example.app.R
import com.example.app.databinding.ActivityUserDataBinding
import com.example.app.shared_prefs_singleton.db.UserDatabase
import com.example.app.shared_prefs_singleton.db.UserInfo
import com.example.app.shared_prefs_singleton.db.client.UserDatabaseClientImpl
import com.example.app.shared_prefs_singleton.dialog.ProfileChooseDialog
import com.example.app.shared_prefs_singleton.utils.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class UserDataActivity : AppCompatActivity() {

    // 로그인 이후에 유저 정보가 보여질 화면.
    private val binding: ActivityUserDataBinding by lazy {
        ActivityUserDataBinding.inflate(layoutInflater)
    }

    private val userDatabaseClient: UserDatabaseClientImpl by lazy {
        UserDatabaseClientImpl(UserDatabase.getInstance(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Preferences.rememberMe) {
            binding.rememberMe.isChecked = true
        }
        binding.userProfileImage.setImageResource(Preferences.userProfile)

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
            lifecycleScope.launch {
                Preferences.userProfile = showDialog()
                binding.userProfileImage.setImageResource(Preferences.userProfile)
                updateProfile()
            }
        }

        binding.logOutBtn.setOnClickListener {
            Preferences.userPw = ""
            val intent = Intent(this@UserDataActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun showDialog(): Int = withContext(Dispatchers.Main) {
        return@withContext suspendCancellableCoroutine { continuation ->
            val dialog = ProfileChooseDialog(this@UserDataActivity)
            dialog.onValueChangedListener = { value ->
                continuation.resume(value)
                Log.d("Dialog Value is....", value.toString())
            }
            dialog.show()
            continuation.invokeOnCancellation { dialog.dismiss() }
        }
    }

    private suspend fun updateProfile() = withContext(Dispatchers.Default){
        userDatabaseClient.updateProfile(Preferences.userId, Preferences.userProfile)
    }
}