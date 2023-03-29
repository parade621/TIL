package com.example.app.shared_prefs_singleton.dialog

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.R
import com.example.app.databinding.FragmentProfileChooseDialogBinding
import com.example.app.shared_prefs_singleton.utils.Preferences
import com.example.app.shared_prefs_singleton.utils.UserDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileChooseDialog() : AppCompatActivity() {

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
            onclick(R.drawable.blue_profile)
        }
        binding.redProfile.setOnClickListener {
            onclick(R.drawable.red_profile)
        }
        binding.orangeProfile.setOnClickListener {
            onclick(R.drawable.orange_profile)
        }
    }
    fun onclick(res: Int){
        Preferences.userProfile = res
        lifecycleScope.launch(Dispatchers.Default){
            UserDB.db.updateProfile(Preferences.userId, Preferences.userProfile)
        }
        finish()
    }
}
