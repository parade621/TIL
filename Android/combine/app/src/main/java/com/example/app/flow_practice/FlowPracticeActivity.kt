//package com.example.app.flow_practice
//
//import android.content.Intent
//import android.location.Location
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import androidx.core.app.ActivityCompat
//import com.example.app.R
//import com.example.app.databinding.ActivityFlowPracticeBinding
//
//class FlowPracticeActivity : AppCompatActivity() {
//
//    private val binding: ActivityFlowPracticeBinding by lazy{
//        ActivityFlowPracticeBinding.inflate(layoutInflater)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(
//                android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ), 0
//        )
//
//        binding.start.setOnClickListener{
//            Intent(applicationContext, LocationService::class.java).apply {
//                action = LocationService.ACTION_START
//                startService(this)
//            }
//        }
//
//        binding.stop.setOnClickListener{
//            Intent(applicationContext, LocationService::class.java).apply {
//                action = LocationService.ACTION_STOP
//                startService(this)
//            }
//        }
//
//    }
//}