package com.example.app.shared_prefs_singleton.ui.adapter

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.app.databinding.LvItemBinding
import com.example.app.shared_prefs_singleton.data.KeyValue

class MyPrefsAdapter :
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
        val currentKeyValue = getItem(position)
        holder.bind(currentKeyValue)
        if(currentKeyValue.key == "userProfile") {
            holder.showDialog(currentKeyValue)
        }
    }
    private var onItemClickListener: ((KeyValue) -> Unit)? = null

    fun setOnItemClickListener(listener: (KeyValue) -> Unit) {
        onItemClickListener = listener
    }
}