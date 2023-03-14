package com.example.layoutex1.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.app.databinding.RvItemsBinding

class itemViewHolder(
    private val binding: RvItemsBinding
):RecyclerView.ViewHolder(binding.root) {
    fun bind(text: String){
        itemView.apply{
            binding.tvItem.text = text
        }
    }
}