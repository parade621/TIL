package com.example.app.shared_prefs_singleton.dialog

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import com.example.app.R
import com.example.app.databinding.FragmentProfileChooseDialogBinding
import com.example.app.shared_prefs_singleton.utils.Preferences

class ProfileChooseDialog() : Activity() {

    private val binding: FragmentProfileChooseDialogBinding by lazy {
        FragmentProfileChooseDialogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val params = window.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params

        binding.blueProfile.setOnClickListener {
            Preferences.userProfile = R.drawable.blue_profile
            finish()
        }
        binding.redProfile.setOnClickListener {
            Preferences.userProfile = R.drawable.red_profile
            finish()
        }
        binding.orangeProfile.setOnClickListener {
            Preferences.userProfile = R.drawable.orange_profile
            finish()
        }
    }
}
