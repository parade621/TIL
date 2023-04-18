package com.example.app.shared_prefs_singleton.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.Utils.DatabaseManager
import com.example.app.databinding.ActivitySharedPrefsBinding
import com.example.app.shared_prefs_singleton.data.TasksRepository
import com.example.app.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedPrefsActivity : AppCompatActivity() {

    // splash activity 역할을 수행할 예정.

    private val binding: ActivitySharedPrefsBinding by lazy {
        ActivitySharedPrefsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        TasksRepository.clearTask()

        lifecycleScope.launch(Dispatchers.IO) {
            if (DataStoreManager.rememberMe) {
                val user = DatabaseManager.getUserById(DataStoreManager.userId)!!
                if(!user.userTask.isNullOrEmpty()){
                    for(i in user.userTask!!){
                        TasksRepository.addTask(i)
                    }
                }
                toTasksActivity(this@SharedPrefsActivity)
            } else {
                toLogInActivity(this@SharedPrefsActivity)
            }
        }
    }

    private suspend fun toTasksActivity(context: Context) = withContext(Dispatchers.Main) {
        val intent = Intent(context, TasksActivity::class.java)
        context.startActivity(intent)
        finish()
    }

    private suspend fun toLogInActivity(context: Context) = withContext(Dispatchers.Main) {
        val intent = Intent(context, LogInActivity::class.java)
        context.startActivity(intent)
        finish()
    }
}