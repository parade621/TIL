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
import com.example.app.camera_view.service.GpsTracker
import com.example.app.camera_view.util.ImageUtil
import com.example.app.camera_view.util.ImageUtil.getOrientationFromBitmap
import com.example.app.camera_view.util.ImageUtil.rotateBitmap
import com.example.app.camera_view.util.ImageUtil.rotateBitmapSimple
import com.example.app.camera_view.util.ImageUtil.textInsertImage
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
    private val savedBitmapList = mutableListOf<Bitmap>()
    private var gpsTracker: GpsTracker? = null

    // 갤러리에서 사진을 가져옵니다.
    private val requestGalleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            try {
                val option = BitmapFactory.Options()
                var filePath: String? = null

                // 선택한 이미지의 경로 가져오기
                val selectedImageUri = activityResult.data?.data
                val selectedImagePath = selectedImageUri?.path
                //val selectedImageName = activityResult.data?.data?.lastPathSegment
                val cursor = selectedImageUri?.let {
                    contentResolver.query(
                        it,
                        arrayOf(MediaStore.Images.Media.DATA),
                        null,
                        null,
                        null
                    )
                }
                cursor?.use {
                    it.moveToFirst()
                    val pathIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = it.getString(pathIndex)
                    Log.d(TAG, "Selected file path: $filePath")
                }

                // 이미지 로드
                val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                var bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream?.close()

            if(ImageUtil.rotate_check_EXIF(filePath!!)){
                Log.d(TAG, "이미지가 회전되어있음.")
                bitmap = rotateBitmapSimple(bitmap!!, 90f)
            }

                uploadImage(bitmap!!, getLatestGpsInfo())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private val gpsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (checkLocationServicesStatus()) {
                    Log.d(TAG, "onActivityResult : GPS 활성화 되있음")
                    checkRunTimePermission()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 기능을 수행하기 위해 사용자에게 위치, 카메라 권한 요청
        requestLocationPermission()

        // GPS 활성화 여부 검사
        checkLocationServicesStatus()

        binding.shutter.setOnSingleClickListener {
            if (!isFullPhoto()) {
                lifecycleScope.launch {
                    val srcBitmap = binding.cameraView.capture()
                    if (srcBitmap != null) {
                        val forLog = saveBitmapToFile(srcBitmap)
                        uploadImage(srcBitmap, getLatestGpsInfo())
                        Log.d(TAG, forLog)
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

    private fun isFullPhoto(): Boolean {
        return savedBitmapList.size == 4
    }

    private fun getLatestGpsInfo(): String {
        gpsTracker = GpsTracker(binding.root.context)
        var latitude = gpsTracker!!.getLatitude()
        var longitude = gpsTracker!!.getLongtitude()
        var address = getCurrentAddress(latitude, longitude)

        return address
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
        // 년-월-일만 파일명으로 저장
        //val name = "IMG_${SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA).format(Date())}.jpg"

        return ImageUtil.saveBitmapToJpeg(this, bitmap, name)
    }

    private fun uploadImage(srcBitmap: Bitmap, address: String?) {
        // 주소 정보 갱신하는 것 완료.
        Toast.makeText(binding.root.context, address, Toast.LENGTH_SHORT).show()
        // textInsertImage()
        when (savedBitmapList.size) {
            0 -> {
//                Glide.with(this@CameraActivity)
//                    .load(photoFilePath)
//                    .into(binding.recentPhoto)
                binding.recentPhoto.setImageBitmap(srcBitmap)
            }
            1 -> {
//                Glide.with(this@CameraActivity)
//                    .load(photoFilePath)
//                    .into(binding.recentPhoto2)
                binding.recentPhoto2.setImageBitmap(srcBitmap)
            }
            2 -> {
//                Glide.with(this@CameraActivity)
//                    .load(photoFilePath)
//                    .into(binding.recentPhoto3)
                binding.recentPhoto3.setImageBitmap(srcBitmap)
            }
            3 -> {
//                Glide.with(this@CameraActivity)
//                    .load(photoFilePath)
//                    .into(binding.recentPhoto4)
                binding.recentPhoto4.setImageBitmap(srcBitmap)
            }
        }
        savedBitmapList.add(srcBitmap)
    }

    // 현재 위치 정보 얻어오기
    fun getCurrentAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this@CameraActivity, Locale.getDefault())
        var addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            // 네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        if (addresses == null || addresses.isEmpty()) {
            return "주소 미발견"
        }

        val address: Address = addresses[0]
        return address.getAddressLine(0).toString() + "\n"
    }


    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
    }

    // 퍼미션 체크 함수 수정
    private fun checkRunTimePermission() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val hasCamreaPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val hasReadMediaImagePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCamreaPermission == PackageManager.PERMISSION_GRANTED &&
            hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
            hasReadMediaImagePermission == PackageManager.PERMISSION_GRANTED
        ) {
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
                } else {
                    // 권한 거부됨
                    Log.d(TAG, "onRequestPermissionsResult : 권한 거부됨")
                }
                return
            }
        }
    }

    // gps 권한 허용여부 체크
    // LocationManager 초기화
    private fun checkLocationServicesStatus(): Boolean {
        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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
