package com.example.app.camera_view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.app.camera_view.util.PermissionUtil
import com.example.app.camera_view.util.setOnSingleClickListener
import com.example.app.databinding.ActivityCameraBinding
import kotlinx.coroutines.launch

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private val savedBitmapList = mutableListOf<Bitmap>()
    private val requestGalleryLauncher = registerForActivityResult(
        // 이거 지금 문제있습니다. 사진이 한칸씩 뛰어 넘어서 저장됨.
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        try {
            val option = android.graphics.BitmapFactory.Options()

            var inputStream = contentResolver.openInputStream(activityResult!!.data!!.data!!)
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream, null, option)
            inputStream!!.close()

            // 갤러리에서 가져온 이미지가 회전되어있어, 90도로 회전해줍니다.
            // exif에서 정보 받아서 이미지 회전여부 판단할것.(구현해야할 거 리스트)
            val rotateBitmap =
                com.example.app.camera_view.util.ImageUtil.rotateBitmap(bitmap!!, 90f)
            uploadImage(bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        permissionCheck()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            val latitude = location.latitude
            val longitude = location.longitude
            Log.d("이글봐글", "${latitude} ${longitude}")
        }

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

        binding.gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            requestGalleryLauncher.launch(intent)
        }

        binding.cameraView.binding(this)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(){
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0f,
            locationListener!!
        )
    }

    private fun getLatestGpsLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val lastKnownLocation =
                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (lastKnownLocation != null) {
                val latitude = lastKnownLocation.latitude
                val longitude = lastKnownLocation.latitude
                Log.d(TAG, "${latitude} ${longitude}")
            }
        }
    }

    private fun isFullPhoto(): Boolean {
        return savedBitmapList.size == 4
    }

//    private fun showPreview(bitmap: Bitmap) {
//        val intent = Intent(this, PreviewActivity::class.java)
//        intent.putExtra("bitmapInfo", BitmapConverter.bitmapToString(bitmap))
//        startActivity(intent)
//    }

    private fun uploadImage(srcBitmap: Bitmap) {
        // 아래 사진에 텍스트를 넣는 기능은 서비스 스터디 후, 완성할 것.
        // textInsertImage()
        when (savedBitmapList.size) {
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
        savedBitmapList.add(srcBitmap)
    }

    private fun permissionCheck() {
        var permissionList =
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
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

    companion object {
        private const val TAG = "Camera_App"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
