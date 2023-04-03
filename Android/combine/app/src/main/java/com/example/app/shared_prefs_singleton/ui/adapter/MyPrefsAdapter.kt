package com.example.app.shared_prefs_singleton.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app.MyApplication
import com.example.app.databinding.LvItemBinding
import com.example.app.shared_prefs_singleton.data.KeyValue
import com.example.app.shared_prefs_singleton.dialog.DisplayProfileDialog

class MyPrefsAdapter(val context: Context) :
    ListAdapter<KeyValue, MyPrefsAdapter.MyPrefsViewHolder>(object : DiffUtil.ItemCallback<KeyValue>() {

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
        holder.itemView.setOnClickListener {
            if(currentKeyValue.key == "userProfile"){
                DisplayProfileDialog(currentKeyValue.value as Int).show(
                    (context as AppCompatActivity).supportFragmentManager,
                ""
                )
            }
        }
    }

    inner class MyPrefsViewHolder(
        private val binding: LvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(keyValue: KeyValue) {
            binding.prefKey.text = keyValue.key
            binding.prefValue.text = keyValue.value.toString()
        }
    }
}