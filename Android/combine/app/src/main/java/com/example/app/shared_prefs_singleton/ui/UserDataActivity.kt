package com.example.app.shared_prefs_singleton.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.example.app.R
import com.example.app.databinding.ActivityUserDataBinding
import com.example.app.shared_prefs_singleton.dialog.ProfileChooseDialog
import com.example.app.shared_prefs_singleton.utils.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataActivity : AppCompatActivity() {

    // 로그인 이후에 유저 정보가 보여질 화면.
    private val binding: ActivityUserDataBinding by lazy{
        ActivityUserDataBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Preferences.rememberMe){
            binding.rememberMe.isChecked = true
        }
        binding.userIdInfo.text = String.format(resources.getString(R.string.user_id,Preferences.userId))

        binding.userProfileImage.setOnClickListener {
            lifecycleScope.launch {
                ProfileChooseDialog().show(supportFragmentManager,"")
                withContext(Dispatchers.Main) {
                    binding.userProfileImage.setImageResource(Preferences.userProfile)
                }

            }
        }
    }
}