package com.example.app.shared_prefs_singleton.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.LvItemBinding
import com.example.app.shared_prefs_singleton.data.KeyValue

class MyPrefsViewHolder(
    private val binding: LvItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(keyValue:KeyValue){
        binding.prefKey.text = keyValue.key
        binding.prefValue.text = keyValue.value.toString()
    }
}