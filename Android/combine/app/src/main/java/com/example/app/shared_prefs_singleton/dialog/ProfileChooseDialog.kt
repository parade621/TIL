package com.example.app.shared_prefs_singleton.dialog

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.MyApplication
import com.example.app.R
import com.example.app.databinding.FragmentProfileChooseDialogBinding
import com.example.app.shared_prefs_singleton.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileChooseDialog() : AppCompatActivity() {

    private val binding: FragmentProfileChooseDialogBinding by lazy {
        FragmentProfileChooseDialogBinding.inflate(layoutInflater)
    }
    private val dataBase = MyApplication.getInstance().getDataBase()

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

    fun onclick(res: Int) {
        DataStoreManager.setUserProfile(res)
        lifecycleScope.launch(Dispatchers.Default) {
            dataBase.updateProfile(DataStoreManager.userId, DataStoreManager.userProfile)
        }
        finish()
    }
}
