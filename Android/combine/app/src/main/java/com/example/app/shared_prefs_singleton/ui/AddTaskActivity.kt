package com.example.app.shared_prefs_singleton.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app.R
import com.example.app.databinding.ActivityAddTaskBinding
import com.example.app.databinding.ActivityTasksBinding

class AddTaskActivity : AppCompatActivity() {
    private val binding: ActivityAddTaskBinding by lazy{
        ActivityAddTaskBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}