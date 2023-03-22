package com.example.servicetest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.servicetest.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var gpsTracker: GpsTracker? = null

    private val gpsActivityResultLauncher =
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

        // GPS를 사용하기 위해 사용자에게 위치 권한 요청
        requestLocationPermission()

        // GPS 활성화 여부 검사
        checkLocationServicesStatus()

        gpsTracker = GpsTracker(binding.root.context)

        binding.button.setOnClickListener {
            var latitude = gpsTracker!!.getLatitude()
            var longitude = gpsTracker!!.getLongtitude()

            var address = getCurrentAddress(latitude,longitude)
            binding.textview.setText(address)

            Toast.makeText(this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        }

    }

    private fun requestLocationPermission(){
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
    }

    private fun checkRunTimePermission() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
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
                // 사용자가 권한 거부를 한 번 이상 한 경우
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

    private fun checkGpsEnabled() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        gpsActivityResultLauncher.launch(intent)
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
                    Log.d(TAG, "onRequestPermissionsResult : 퍼미션 허용됨")
                } else {
                    // 권한 거부됨
                    Log.d(TAG, "onRequestPermissionsResult : 퍼미션 거부됨")
                }
                return
            }
        }
    }

    // 위치 서비스 상태를 확인합니다.
    private fun checkLocationServicesStatus():Boolean{
        val locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getCurrentAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
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


    companion object{
        private const val TAG = "GPS_TEST"
        private const val GPS_ENABLE_REQUEST_CODE = 2001
        private const val PERMISSIONS_REQUEST_CODE = 100
        private val REQUIRED_PERMISSIONS= arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}