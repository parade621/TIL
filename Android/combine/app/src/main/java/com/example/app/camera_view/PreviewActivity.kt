package com.example.app.camera_view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.app.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {

    private val binding : ActivityPreviewBinding by lazy{
        ActivityPreviewBinding.inflate(layoutInflater)
    }

    private var receivedUri : Uri? = null
    private var mScaleGestureDetector: ScaleGestureDetector?=null
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uriString = intent.getStringExtra("uriInfo")
        Log.d("PreviewActivity", uriString.toString())

        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        receivedUri = Uri.parse(uriString)

        binding.PhotoPreview.setImageURI(receivedUri)
    }

    // 제스처 이벤트가 발생하면 실행되는 메소드
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //제스처 이벤트를 처리
        mScaleGestureDetector!!.onTouchEvent(event!!)
        return true
    }

    // 제스쳐 이벤트를 처리하는 클래스
    inner class ScaleListener: ScaleGestureDetector.SimpleOnScaleGestureListener(){
        override fun onScale(detector: ScaleGestureDetector): Boolean {

            scaleFactor *= mScaleGestureDetector!!.scaleFactor

            // 최소 0.1, 최대 2배
            scaleFactor = Math.max(0.9f, Math.min(scaleFactor, 3.0f))

            // 이미지에 적용
            binding.PhotoPreview.scaleX = scaleFactor
            binding.PhotoPreview.scaleY = scaleFactor

            return true
        }
    }
}