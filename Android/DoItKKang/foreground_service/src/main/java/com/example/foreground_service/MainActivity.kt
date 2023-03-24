package com.example.foreground_service

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foreground_service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var serviceIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 서비스 시작
        binding.startBtn.setOnClickListener {
            startService()
            Toast.makeText(applicationContext, "서비스 시작", Toast.LENGTH_SHORT).show()
        }

        // 서비스 취소
        binding.stopBtn.setOnClickListener {
            Toast.makeText(applicationContext, "서비스 중지", Toast.LENGTH_SHORT).show()
            stopService()
        }
    }

    /**
     * 알림서비스 실행
     */
    private fun startService() {
        serviceIntent = Intent(this, MyService::class.java)
        startService(serviceIntent)
    }

    /**
     * 알림서비스 중지
     */
    private fun stopService() {
        serviceIntent = Intent(this, MyService::class.java)
        stopService(serviceIntent)
    }
}