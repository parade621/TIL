package com.example.app.shared_prefs_singleton.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.databinding.ActivityAddTaskBinding
import com.example.app.shared_prefs_singleton.data.Task
import com.example.app.shared_prefs_singleton.data.TaskPriority
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.shared_prefs_singleton.utils.hideKeyboardOnTouchOutside
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private val binding: ActivityAddTaskBinding by lazy {
        ActivityAddTaskBinding.inflate(layoutInflater)
    }

    private lateinit var taskPriority: TaskPriority
    private lateinit var myDeadline: Date
    private val simpleDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // activityLauncher init

        binding.titleArea.apply {
            isCounterEnabled = true
            counterMaxLength = 20
        }

        // 350자 이상 입력 못하게 설정
        binding.bodyArea.apply {
            isCounterEnabled = true
            counterMaxLength = 350
            editText?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    if (editable?.length ?: 0 > 350) {
                        val myText = editable?.substring(0, 350)
                        binding.bodyArea.editText?.setText(myText)
                        binding.bodyArea.editText?.setSelection(myText?.length ?: 0)
                        binding.bodyArea.error = "최대 350자까지 입력 가능합니다."
                    } else {
                        binding.bodyArea.error = null
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
        // 우선순위 설정
        binding.priorityLow.setOnClickListener {
            taskPriority = TaskPriority.LOW
            binding.priorityLow.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            binding.priorityMedium.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.lightGray
                )
            )
            binding.priorityHigh.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
        }
        binding.priorityMedium.setOnClickListener {
            taskPriority = TaskPriority.MEDIUM
            binding.priorityMedium.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            binding.priorityLow.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
            binding.priorityHigh.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
        }
        binding.priorityHigh.setOnClickListener {
            taskPriority = TaskPriority.HIGH
            binding.priorityHigh.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            binding.priorityLow.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
            binding.priorityMedium.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.lightGray
                )
            )
        }

        binding.deadline.setOnClickListener {
            showDatePickerDialog()
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
        binding.saveBtn.setOnClickListener {
            val task = Task(
                name = binding.title.text.toString(),
                deadline = myDeadline,
                content = binding.body.text.toString(),
                priority = taskPriority,
            )
            TasksRepository.addTask(task)
            TasksRepository.forLog("add")
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                myDeadline = simpleDateFormat.parse("$year-${month + 1}-$day")
                val dateFormat = java.text.SimpleDateFormat("MMM d, yyyy", Locale.KOREA)
                binding.deadline.text = dateFormat.format(myDeadline)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        hideKeyboardOnTouchOutside(this, event)
        return super.dispatchTouchEvent(event)
    }
}