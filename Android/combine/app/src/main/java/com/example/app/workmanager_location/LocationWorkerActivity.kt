package com.example.app.workmanager_location

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivityLocationWorkerBinding

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