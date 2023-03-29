package com.example.app.shared_prefs_singleton.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.databinding.ActivityPreferenceListBinding
import com.example.app.databinding.LvItemBinding
import com.example.app.shared_prefs_singleton.data.KeyValue
import com.example.app.shared_prefs_singleton.ui.adapter.MyPrefsViewHolder
import com.example.app.shared_prefs_singleton.utils.Preferences

class PreferenceListActivity : AppCompatActivity() {

    private val binding: ActivityPreferenceListBinding by lazy {
        ActivityPreferenceListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val allEntries: Map<String, *> = Preferences.preferences.all

        val keyValues: List<KeyValue> = allEntries.map {
            KeyValue(it.key, it.value)
        }

        val myAdapter = object :
            ListAdapter<KeyValue, MyPrefsViewHolder>(object : DiffUtil.ItemCallback<KeyValue>() {
                override fun areItemsTheSame(oldItem: KeyValue, newItem: KeyValue): Boolean {
                    return oldItem.key == newItem.key
                }

                override fun areContentsTheSame(oldItem: KeyValue, newItem: KeyValue): Boolean {
                    return oldItem == newItem
                }
            }) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPrefsViewHolder {
                return MyPrefsViewHolder(
                    LvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            override fun onBindViewHolder(holder: MyPrefsViewHolder, position: Int) {
                holder.bind(getItem(position))
            }
        }

        myAdapter.submitList(keyValues)

        binding.rvPrefList.apply {
            setHasFixedSize(true)
            binding.rvPrefList.layoutManager = LinearLayoutManager(this@PreferenceListActivity,LinearLayoutManager.VERTICAL, false)
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