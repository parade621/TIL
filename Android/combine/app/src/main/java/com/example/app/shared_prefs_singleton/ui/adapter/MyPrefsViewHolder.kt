package com.example.app.shared_prefs_singleton.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.LvItemBinding
import com.example.app.shared_prefs_singleton.data.KeyValue
import com.example.app.shared_prefs_singleton.dialog.DisplayProfileDialog

class MyPrefsViewHolder(
    private val binding: LvItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(keyValue: KeyValue) {
        binding.prefKey.text = keyValue.key
        binding.prefValue.text = keyValue.value.toString()
    }

    fun showDialog(keyValue: KeyValue){
        binding.prefValue.setOnClickListener {
            DisplayProfileDialog(keyValue.value as Int).show((binding.prefValue.context as AppCompatActivity).supportFragmentManager, "")
        }
    }
}