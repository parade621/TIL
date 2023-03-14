package com.example.app.thread_timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.databinding.DataBindingUtil.setContentView
import com.example.app.R
import com.example.app.databinding.ActivityTtactivityBinding
import kotlin.concurrent.thread

class TTActivity : AppCompatActivity() {

    private val binding : ActivityTtactivityBinding by lazy{
        ActivityTtactivityBinding.inflate(layoutInflater)
    }

    private var total = 0
    private var started = false

    val handler = object : Handler() { //Handler 생성
        override fun handleMessage(msg: Message){ // 화면에 시간을 출력하는 Handler
            val minute = String.format("%02d", total / 60) // min
            val second = String.format("%02d", total % 60) // sec
            binding.tvTimer.text = "$minute:$second" // textView.text 속성 변경
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply{
            mainActivity = this@TTActivity
        }
    }

    fun onStartBtnClick(){
        if (started == false){
            started = true
            total = 0
            binding.tvTimer.text = "00:00"

            // runOnUiThread
            thread(start = true){
                while(started){
                    Thread.sleep(1000)
                    if(started){
                        total += 1
                        handler?.sendEmptyMessage(0)
                    }
                }
            }
        }
    }

    fun onStopBtnClick(){
        started = false // started 값을 false로 변경
    }

}