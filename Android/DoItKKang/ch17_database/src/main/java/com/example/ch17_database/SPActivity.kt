package com.example.ch17_database

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ch17_database.databinding.ActivitySpactivityBinding

class SPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivitySpactivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 액티비티의 데이터 저장
        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        // 앱 전체의 데이터 저장
        val sharedPrefApp = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        // 프리퍼런스에 데이터 저장
        sharedPrefApp.edit().run{
            putString("data1", "hello")
            putInt("data2", 10)
            commit()
        }
        // 프리퍼런스에서 데이터 가져오기
        val data1 = sharedPrefApp.getString("data1", "world")
        val data2 =sharedPrefApp.getInt("data2", 10)
    }
}