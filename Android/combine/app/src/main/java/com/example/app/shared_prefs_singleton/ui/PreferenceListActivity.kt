package com.example.app.shared_prefs_singleton.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.databinding.ActivityPreferenceListBinding
import com.example.app.shared_prefs_singleton.data.KeyValue
import com.example.app.shared_prefs_singleton.ui.adapter.MyPrefsAdapter
import com.example.app.shared_prefs_singleton.ui.viewmodel.MyViewModel
import com.example.app.shared_prefs_singleton.utils.Preferences

class PreferenceListActivity : AppCompatActivity() {

    private val binding: ActivityPreferenceListBinding by lazy {
        ActivityPreferenceListBinding.inflate(layoutInflater)
    }
    private val myViewModel : MyViewModel by lazy{
        MyViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myAdapter = MyPrefsAdapter().apply {
            submitList(myViewModel.keyValues)
        }

        binding.rvPrefList.apply {
            setHasFixedSize(true)
            binding.rvPrefList.layoutManager = LinearLayoutManager(
                this@PreferenceListActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(
                DividerItemDecoration(
                    this@PreferenceListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = myAdapter
        }
    }
}