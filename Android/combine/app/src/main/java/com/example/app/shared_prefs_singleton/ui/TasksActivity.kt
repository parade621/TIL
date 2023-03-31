package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.app.databinding.ActivityTasksBinding
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.shared_prefs_singleton.ui.adapter.TasksAdapter
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModel
import kotlinx.coroutines.launch

class TasksActivity : AppCompatActivity() {

    private val binding: ActivityTasksBinding by lazy {
        ActivityTasksBinding.inflate(layoutInflater)
    }
    private val myAdapter = TasksAdapter()
    private val myViewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myViewModel.taskUiModel.observe(this) {
            myAdapter.submitList(it.tasks)
        }
        setupRecyclerView()

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, UserDataActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.addFab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupRecyclerView() {
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        binding.list.adapter = myAdapter
    }
}