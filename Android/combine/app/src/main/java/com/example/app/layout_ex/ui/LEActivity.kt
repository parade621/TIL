package com.example.layoutex1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityLeactivityBinding
import com.example.app.databinding.ActivityMainBinding

class LEActivity : AppCompatActivity() {

    private val binding: ActivityLeactivityBinding by lazy{
        ActivityLeactivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}