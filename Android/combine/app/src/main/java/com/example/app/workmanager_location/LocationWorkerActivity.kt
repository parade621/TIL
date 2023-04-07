package com.example.app.workmanager_location

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app.Utils.DataStoreManager
import com.example.app.databinding.ActivityLocationWorkerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationWorkerActivity : AppCompatActivity() {

    private lateinit var interactor: LocationInteractor

    private val binding: ActivityLocationWorkerBinding by lazy {
        ActivityLocationWorkerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            initializeLocationInteractor()
            LocationWorker.run()
        }
    }


    private fun initializeLocationInteractor(){
        interactor = LocationInteractor(this.applicationContext)
        interactor.init()
    }

}