package com.example.app.fragmentlifecycletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.app.R
import com.example.app.dialogs.FirstFragment

class FLTActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fltactivity)

        supportFragmentManager.beginTransaction()
            .add(R.id.frameContainer, FirstFragment())
            .commit()

    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG ", "onStart(), MainActivity")
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG ", "onResume(), MainActivity")
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG ", "onPause(), MainActivity")
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG ", "onStop(), MainActivity")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG ", "onDestroy(), MainActivity")
    }
}
