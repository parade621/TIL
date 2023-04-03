package com.example.app.shared_prefs_singleton.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityPreferenceListBinding
import com.example.app.shared_prefs_singleton.ui.adapter.MyPrefsAdapter
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModel

class PreferenceListActivity : AppCompatActivity() {

    private val binding: ActivityPreferenceListBinding by lazy {
        ActivityPreferenceListBinding.inflate(layoutInflater)
    }
    private val myViewModel: TasksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myAdapter = MyPrefsAdapter(this)
        myViewModel.getKeyValues().observe(this) { keyValues ->
            myAdapter.submitList(keyValues)
        }

        binding.rvPrefList.apply {
            setHasFixedSize(true)
            binding.rvPrefList.layoutManager = LinearLayoutManager(
                this@PreferenceListActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = myAdapter
        }
    }
}