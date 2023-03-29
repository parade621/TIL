package com.example.app.customdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.app.databinding.ActivityCustomDialogBinding

class CustomDialogActivity : AppCompatActivity() {

    private val binding: ActivityCustomDialogBinding by lazy {
        ActivityCustomDialogBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            customDialogActivity = this@CustomDialogActivity
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("바바", "이건 멈춤")
    }
}