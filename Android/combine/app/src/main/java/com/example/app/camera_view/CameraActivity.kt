package com.example.app.camera_view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.app.camera_view.service.GpsTracker
import com.example.app.camera_view.util.ImageUtil
import com.example.app.camera_view.util.setOnSingleClickListener
import com.example.app.databinding.ActivityCameraBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }
    private val savedBitmapList = mutableListOf<Bitmap>()
    private var gpsTracker: GpsTracker? = null

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
            uploadImage(bitmap, null)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val gpsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
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
                        saveBitmapAsFile(srcBitmap)
                        uploadImage(srcBitmap, getLatestGpsInfo())
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

    private fun getLatestGpsInfo(): String{
        gpsTracker = GpsTracker(binding.root.context)
        var latitude = gpsTracker!!.getLatitude()
        var longitude = gpsTracker!!.getLongtitude()
        var address = getCurrentAddress(latitude, longitude)

        return address
    }

    fun saveBitmapAsFile(bitmap: Bitmap){
        // 시간도 함께 명칭에 포함
        val name = "IMG_${SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA).format(System.currentTimeMillis())}.jpg"
        // 년-월-일만 파일명으로 저장
        //val name = "IMG_${SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA).format(Date())}.jpg"

        ImageUtil.saveBitmapToJpeg(this, bitmap,name)
    }

    private fun uploadImage(srcBitmap: Bitmap, address: String?) {
        // 주소 정보 갱신하는 것 완료.
        Toast.makeText(binding.root.context, address, Toast.LENGTH_SHORT).show()
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
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val hasCamreaPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCamreaPermission == PackageManager.PERMISSION_GRANTED
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
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
