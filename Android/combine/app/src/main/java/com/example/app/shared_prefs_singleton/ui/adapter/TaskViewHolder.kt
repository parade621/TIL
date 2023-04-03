package com.example.app.shared_prefs_singleton.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.databinding.TaskItemsBinding
import com.example.app.shared_prefs_singleton.data.Task
import com.example.app.shared_prefs_singleton.data.TaskPriority
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(
    private val binding: TaskItemsBinding
):RecyclerView.ViewHolder(binding.root) {

    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.KOREA)

    fun bind(todo: Task) {
        binding.task.text = todo.title
        setTaskPriority(todo)
        binding.deadline.text = dateFormat.format(todo.deadline)
        val color = if (todo.completed) {
            R.color.greyAlpha
        } else {
            R.color.white
        }
        itemView.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                color
            )
        )
    }

    private fun setTaskPriority(todo: Task) {
        binding.priority.text = itemView.context.resources.getString(
            R.string.priority_value,
            todo.priority.name
        )
        val textColor = when (todo.priority) {
            TaskPriority.HIGH -> R.color.red
            TaskPriority.MEDIUM -> R.color.yellow
            TaskPriority.LOW -> R.color.green
        }
        binding.priority.setTextColor(ContextCompat.getColor(itemView.context, textColor))
    }

    // 새롭게 작성해본 스타일.
    // 구글 공식문서에서 이런 방식으로 작성되어있어서 따라해봤습니다.
    companion object {
        fun create(parent: ViewGroup): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_items, parent, false)
            val binding = TaskItemsBinding.bind(view)
            return TaskViewHolder(binding)
        }
    }
}