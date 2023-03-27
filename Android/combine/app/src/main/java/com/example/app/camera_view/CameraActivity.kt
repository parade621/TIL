package com.example.app.camera_view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.app.camera_view.gps.GpsData
import com.example.app.camera_view.service.GpsTracker
import com.example.app.camera_view.util.ImageUtil
import com.example.app.camera_view.util.ImageUtil.getOrientationFromBitmap
import com.example.app.camera_view.util.ImageUtil.rotateBitmap
import com.example.app.camera_view.util.ImageUtil.rotateBitmapSimple
import com.example.app.camera_view.util.ImageUtil.textInsertImage
import com.example.app.camera_view.util.hasPermissions
import com.example.app.camera_view.util.setOnSingleClickListener
import com.example.app.databinding.ActivityCameraBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }
    private val filePathList = mutableListOf<String>()
    //private var gpsTracker: GpsTracker? = null

    // 갤러리에서 사진을 가져옵니다.
    private val requestGalleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
           if(activityResult.resultCode == RESULT_OK){
               val data : Intent? = activityResult.data
               if(data!=null){
                   val selectedImageUri = data.data
                   if(selectedImageUri != null){
                       try{
                           Glide.with(this@CameraActivity)
                               .asBitmap()
                               .load(selectedImageUri)
                               .listener(object: RequestListener<Bitmap>{
                                   override fun onLoadFailed(
                                       e: GlideException?,
                                       model: Any?,
                                       target: Target<Bitmap>?,
                                       isFirstResource: Boolean
                                   ): Boolean {
                                       return false
                                   }
                                   override fun onResourceReady(
                                       resource: Bitmap?,
                                       model: Any?,
                                       target: Target<Bitmap>?,
                                       dataSource: DataSource?,
                                       isFirstResource: Boolean
                                   ): Boolean {
                                       if (resource != null) {
                                           lifecycleScope.launch {
                                               val resizeBitmap =
                                                   CameraView.resizeBitmapImage(resource)
                                               if (resizeBitmap != null) {
                                                   uploadImage(resizeBitmap)
                                               }
                                           }
                                       }
                                       return false
                                   }
                               }).submit()
                       }catch(e:java.lang.Exception){
                           e.printStackTrace()
                       }
                   }
               }
           }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 기능을 수행하기 위해 사용자에게 위치, 카메라 권한 요청
        checkRunTimePermission()

        binding.shutter.setOnSingleClickListener {
            if (!isFullPhoto()) {
                lifecycleScope.launch {
                    val srcBitmap = binding.cameraView.capture()
                    if (srcBitmap != null) {
                        uploadImage(srcBitmap)
                    }
                }
            }
        }
        // 갤러리로 이동
        binding.gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            //intent.action = Intent.ACTION_GET_CONTENT
            requestGalleryLauncher.launch(intent)
        }

        binding.recentPhoto.setOnClickListener {
            if (filePathList.size > 0) {
                val intent = Intent(this@CameraActivity, PreviewActivity::class.java)
                intent.putExtra("filePath", filePathList[0])
                startActivity(intent)
            }
        }
        binding.recentPhoto2.setOnClickListener {
            if (filePathList.size > 1) {
                val intent = Intent(this@CameraActivity, PreviewActivity::class.java)
                intent.putExtra("filePath", filePathList[1])
                startActivity(intent)
            }
        }
        binding.recentPhoto3.setOnClickListener {
            if (filePathList.size > 2) {
                val intent = Intent(this@CameraActivity, PreviewActivity::class.java)
                intent.putExtra("filePath", filePathList[2])
                startActivity(intent)
            }
        }
        binding.recentPhoto4.setOnClickListener {
            if (filePathList.size > 3) {
                val intent = Intent(this@CameraActivity, PreviewActivity::class.java)
                intent.putExtra("filePath", filePathList[3])
                startActivity(intent)
            }
        }
        binding.cameraView.binding(this)
    }

    private fun isFullPhoto(): Boolean {
        return filePathList.size == 4
    }

    // 비트맵을 파일로 저장합니다.
    // Qdrive에는 기능이 구현이 되어있으므로, 해당 함수는 CameraView가 아닌, CameraActivity에 저장합니다.
    fun saveBitmapToFile(bitmap: Bitmap): String {
        // 시간도 함께 명칭에 포함
        val name = "IMG_${
            SimpleDateFormat(
                FILENAME_FORMAT,
                Locale.KOREA
            ).format(System.currentTimeMillis())
        }.jpg"

        return ImageUtil.saveBitmapToJpeg(this, bitmap, name)
    }

    private fun uploadImage(srcBitmap: Bitmap) {
        textInsertImage("Test", srcBitmap, this@CameraActivity)
        val filePath = saveBitmapToFile(srcBitmap)
        when (filePathList.size) {
            0 -> {
                binding.recentPhoto.setImageBitmap(srcBitmap)
            }
            1 -> {
                binding.recentPhoto2.setImageBitmap(srcBitmap)
            }
            2 -> {
                binding.recentPhoto3.setImageBitmap(srcBitmap)
            }
            3 -> {
                binding.recentPhoto4.setImageBitmap(srcBitmap)
            }
        }
        filePathList.add(filePath)
    }

    // 퍼미션 체크 함수 수정
    private fun checkRunTimePermission() {
        if (binding.root.context.hasPermissions()) {
            // 이미 권한이 허용됨
            Log.d(TAG, "checkRunTimePermission : 권한 이미 허용됨")
        } else {
            // 권한 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 사용자가 권한 거부를 한 번 이상 한 경우 snackbar 실행
                Snackbar.make(
                    binding.root,
                    "권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해주세요.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("확인") {
                    // 설정 화면으로 이동
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            } else {
                // 권한 요청 팝업 출력
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허용됨
                    Log.d(TAG, "onRequestPermissionsResult : 권한 허용됨")
                    GpsData.startGpsService(this@CameraActivity)
                } else {
                    // 권한 거부됨
                    Log.d(TAG, "onRequestPermissionsResult : 권한 거부됨")
                    checkRunTimePermission()
                }
                return
            }
        }
    }

    companion object {
        private const val TAG = "Camera_App"
        private const val PERMISSIONS_REQUEST_CODE = 100
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }
}
