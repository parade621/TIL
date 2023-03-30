package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.app.R
import com.example.app.databinding.ActivityUserDataBinding
import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.shared_prefs_singleton.dialog.ProfileChooseDialog
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModel
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModelFactory
import com.example.app.shared_prefs_singleton.utils.Preferences

class UserDataActivity : AppCompatActivity() {

    // 로그인 이후에 유저 정보가 보여질 화면.
    private val binding: ActivityUserDataBinding by lazy {
        ActivityUserDataBinding.inflate(layoutInflater)
    }

    private lateinit var myViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(
            this,
            TasksViewModelFactory(TasksRepository)
        ).get(TasksViewModel::class.java)

        if (Preferences.rememberMe) {
            binding.rememberMe.isChecked = true
        }

        binding.rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 현재 체크되어있음
                if (Preferences.rememberMe) {
                    // 이미 체크된 상태면 체크 해제
                    binding.rememberMe.isChecked = false
                    Preferences.rememberMe = false
                } else {
                    // 체크 안되있으면 체크
                    Preferences.rememberMe = true
                }
            } else {
                // 체크 안되어있으면 false
                Preferences.rememberMe = false
            }
        }

        binding.userIdInfo.text =
            String.format(resources.getString(R.string.user_id, Preferences.userId))

        setupFilterListeners(myViewModel)
        setupSort()

        myViewModel.taskUiModel.observe(this) { tasksUiModel ->
            updateSort(tasksUiModel.sortOrder)
            binding.showCompletedSwitch.isChecked = tasksUiModel.showCompleted
        }

        binding.userProfileImage.setOnClickListener {
            showDialog()
        }

        binding.prefList.setOnClickListener {
            val intent = Intent(this@UserDataActivity, PreferenceListActivity::class.java)
            startActivity(intent)
        }

        binding.logOutBtn.setOnClickListener {
            Preferences.userPw = ""
            val intent = Intent(this@UserDataActivity, LogInActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tasksBtn.setOnClickListener {
            val intent = Intent(this, TasksActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun setupFilterListeners(viewModel: TasksViewModel) {
        binding.showCompletedSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.showCompletedTasks(checked)
        }
    }

    private fun setupSort() {
        binding.sortDeadline.setOnCheckedChangeListener { _, checked ->
            myViewModel.enableSortByDeadline(checked)
        }
        binding.sortPriority.setOnCheckedChangeListener { _, checked ->
            myViewModel.enableSortByPriority(checked)
        }
    }

    private fun updateSort(sortOrder: SortOrder) {
        binding.sortDeadline.isChecked =
            sortOrder == SortOrder.BY_DEADLINE || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        binding.sortPriority.isChecked =
            sortOrder == SortOrder.BY_PRIORITY || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
    }

    override fun onResume() {
        super.onResume()
        binding.userProfileImage.setImageResource(Preferences.userProfile)
    }

    private fun showDialog() {
        val intent = Intent(this, ProfileChooseDialog::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        Log.d("바꾸기 누름", "true")
    }

}