package com.example.app.nav_and_sharedviewmodel.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app.R
import com.example.app.databinding.ActivityNsactivityBinding

class NSActivity : AppCompatActivity() {

    private val binding: ActivityNsactivityBinding by lazy{
        ActivityNsactivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}