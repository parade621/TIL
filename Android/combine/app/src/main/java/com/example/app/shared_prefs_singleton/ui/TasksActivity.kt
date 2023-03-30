package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.app.R
import com.example.app.databinding.ActivityTasksBinding
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.shared_prefs_singleton.ui.adapter.TaskViewHolder
import com.example.app.shared_prefs_singleton.ui.adapter.TasksAdapter
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModel
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModelFactory
import com.example.app.shared_prefs_singleton.utils.Preferences

class TasksActivity : AppCompatActivity() {

    private val binding: ActivityTasksBinding by lazy{
        ActivityTasksBinding.inflate(layoutInflater)
    }
    private val myAdapter = TasksAdapter()
    private lateinit var myViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(
            this,
            TasksViewModelFactory(TasksRepository)
        ).get(TasksViewModel::class.java)

        setupRecyclerView()

        myViewModel.taskUiModel.observe(this){
            myAdapter.submitList(it.tasks)
            Log.d("myTasksViewModel","isVisible? : ${it.showCompleted}")
        }

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, UserDataActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(){
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        binding.list.adapter = myAdapter
    }
}