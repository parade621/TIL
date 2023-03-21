package com.example.app.camera_view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.lifecycle.lifecycleScope
import com.example.app.camera_view.util.PermissionUtil
import com.example.app.camera_view.util.setOnSingleClickListener
import com.example.app.databinding.ActivityCameraBinding
import kotlinx.coroutines.launch

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }

    private val savedBitmapList = mutableListOf<Bitmap>()
    private lateinit var requestGalleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        permissionCheck()
        requestGalleryLauncher = getPhotoFromGallery()

        binding.shutter.setOnSingleClickListener {
            if(!isFullPhoto()){
                lifecycleScope.launch{
                    val srcBitmap = binding.cameraView.capture()
                    if(srcBitmap != null) uploadImage(srcBitmap)
                }
            }
        }

        binding.gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            requestGalleryLauncher.launch(intent)
        }

        binding.cameraView.binding(this)
    }

    private fun isFullPhoto():Boolean{
        return if(savedBitmapList.size == 4){
            true
        }else{
            false
        }
    }

    private suspend fun uploadImage(srcBitmap: Bitmap){
    // 아래 사진에 텍스트를 넣는 기능은 서비스 스터디 후, 완성할 것.
    // textInsertImage()
        when(savedBitmapList.size){
            0->{
                binding.recentPhoto.setImageBitmap(srcBitmap)
            }
            1->{
                binding.recentPhoto2.setImageBitmap(srcBitmap)
            }
            2->{
                binding.recentPhoto3.setImageBitmap(srcBitmap)
            }
            3->{
                binding.recentPhoto4.setImageBitmap(srcBitmap)
            }
        }
        savedBitmapList.add(srcBitmap)
    }

    private fun permissionCheck() {
        var permissionList =
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        if (!PermissionUtil.checkPermission(binding.root.context, permissionList)) {
            PermissionUtil.requestPermission(this, permissionList)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "권한 승인")
            //startCamera()
        } else {
            //
            Log.e(TAG, "권한 거절")
            onBackPressed()
        }
    }

    private fun getPhotoFromGallery() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        try {
            val option = BitmapFactory.Options()

            var inputStream = contentResolver.openInputStream(activityResult!!.data!!.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
            inputStream!!.close()

            lifecycleScope.launch{
                uploadImage(bitmap!!)
            }
            savedBitmapList.add(bitmap!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "Camera_App"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
