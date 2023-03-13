package com.example.layoutex1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.layoutex1.databinding.RvItemsBinding

class InputTextListAdapter(val items: MutableList<String>)
    : RecyclerView.Adapter<itemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        return itemViewHolder(
            RvItemsBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        Log.d("wow",position.toString())
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}