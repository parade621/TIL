package com.example.app.camera_view

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.lifecycle.lifecycleScope
import com.example.app.camera_view.util.BitmapConverter
import com.example.app.camera_view.util.ImageUtil
import com.example.app.camera_view.util.PermissionUtil
import com.example.app.camera_view.util.setOnSingleClickListener
import com.example.app.databinding.ActivityCameraBinding
import kotlinx.coroutines.launch

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }

    // 위치 정보를 가져오기 위한 locationManager
    private val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

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

        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            val provider = location.provider
            val longitude = location.longitude
            val latitude = location.latitude
            val altitude = location.altitude
        }
        Log.d("위치정보 로그 확인", location.toString())
    }


    private fun isFullPhoto():Boolean{
        return if(savedBitmapList.size == 4){
            true
        }else{
            false
        }
    }

    private suspend fun isValueExist(e: Int):Boolean{
        return if (savedBitmapList[e]!=null){
            true
        }else{
            false
        }
    }

    private fun showPreview(bitmap: Bitmap) {
        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra("bitmapInfo", BitmapConverter.bitmapToString(bitmap))
        startActivity(intent)
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
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
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

            // 갤러리에서 가져온 이미지가 회전되어있어, 90도로 회전해줍니다.
            val rotateBitmap = ImageUtil.rotateBitmap(bitmap!!, 90f)

            lifecycleScope.launch{
                uploadImage(bitmap!!)
            }
            savedBitmapList.add(bitmap!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경될 때 호출되는 콜백 메소드
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현.
            val provider = location.provider // 위치정보
            val longitude = location.longitude // 위도
            val latitude = location.latitude // 경도
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // 위치 공급자 상태가 변경될 때 호출되는 콜백 메소드
        }

        override fun onProviderEnabled(provider: String) {
            // 위치 공급자가 사용 가능해질 때 호출되는 콜백 메소드
        }

        override fun onProviderDisabled(provider: String) {
            // 위치 공급자가 사용 불가능해질 때 호출되는 콜백 메소드
        }
    }

    companion object {
        private const val TAG = "Camera_App"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
