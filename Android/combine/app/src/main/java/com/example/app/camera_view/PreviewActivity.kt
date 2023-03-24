package com.example.app.camera_view

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.bumptech.glide.Glide
import com.example.app.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {

    private val binding : ActivityPreviewBinding by lazy{
        ActivityPreviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val filePath = intent.getStringExtra("filePath")

        Glide.with(this@PreviewActivity)
            .asBitmap()
            .load(filePath)
            .into(binding.ivPhoto)
    }

}

/**
 * photoView 라이브러리 적용(이미지 확대/축소 )
 *
 * https://github.com/Baseflow/PhotoView
 *
 */