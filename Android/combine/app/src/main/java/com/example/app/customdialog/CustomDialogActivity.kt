package com.example.app.customdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}