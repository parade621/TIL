package com.example.app.shared_prefs_singleton.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.app.MyApplication
import com.example.app.R
import com.example.app.databinding.ActivityUserDataBinding
import com.example.app.shared_prefs_singleton.data.SortOrder
import com.example.app.shared_prefs_singleton.dialog.ProfileChooseDialog
import com.example.app.shared_prefs_singleton.ui.viewmodel.TasksViewModel

class UserDataActivity : AppCompatActivity() {

    // 로그인 이후에 유저 정보가 보여질 화면.
    private val binding: ActivityUserDataBinding by lazy {
        ActivityUserDataBinding.inflate(layoutInflater)
    }

    private val myViewModel: TasksViewModel by viewModels()
    private val dataStore = MyApplication.getInstance().getDataStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (dataStore.rememberMe) {
            binding.rememberMe.isChecked = true
        }

        binding.rememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 현재 체크되어있음
                if (dataStore.rememberMe) {
                    // 이미 체크된 상태면 체크 해제
                    binding.rememberMe.isChecked = false
                    dataStore.rememberMe = false
                } else {
                    // 체크 안되있으면 체크
                    dataStore.rememberMe = true
                }
            } else {
                // 체크 안되어있으면 false
                dataStore.rememberMe = false
            }
        }


        binding.userIdInfo.text =
            String.format(
                resources.getString(
                    R.string.user_id,
                    dataStore.userId
                )
            )

        setupFilterListeners(myViewModel)

        myViewModel.initialSetupEvent.observe(this) { initialSetupEvent ->
            updateSort(initialSetupEvent.sortOrder, initialSetupEvent.showCompleted)
            setupSort()
            observePreferenceChanges()
        }

        binding.userProfileImage.setOnClickListener {
            showDialog()
        }

        binding.prefList.setOnClickListener {
            val intent = Intent(this@UserDataActivity, PreferenceListActivity::class.java)
            startActivity(intent)
        }

        binding.logOutBtn.setOnClickListener {
            dataStore.userPw = ""
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
        binding.showCompletedSwitch.setOnCheckedChangeListener { _, checked ->
            myViewModel.showCompletedTasks(checked)
        }
    }

    private fun observePreferenceChanges() {
        myViewModel.taskUiModel.observe(this) { taskUiModel ->
            updateSort(taskUiModel.sortOrder, taskUiModel.showCompleted)
        }
    }

    private fun updateSort(sortOrder: SortOrder, showCompleted: Boolean) {
        with(binding) {
            showCompletedSwitch.isChecked = showCompleted
            binding.sortDeadline.isChecked =
                sortOrder == SortOrder.BY_DEADLINE || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
            binding.sortPriority.isChecked =
                sortOrder == SortOrder.BY_PRIORITY || sortOrder == SortOrder.BY_DEADLINE_AND_PRIORITY
        }
    }

    override fun onResume() {
        super.onResume()
        binding.userProfileImage.setImageResource(dataStore.userProfile)
    }

    private fun showDialog() {
        val intent = Intent(this, ProfileChooseDialog::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
    }

}