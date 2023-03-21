//package com.example.app.camera_view.util
//
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Bundle
//import android.os.IBinder
//import android.os.Messenger
//
//class GPSInfo:Service() {
//
//    // 액티비티에 데이터를 전달하는 메신저
//    lateinit var replyMessenger: Messenger
//
//    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//    override fun onCreate() {
//        super.onCreate()
//        locationManager.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER,
//            1000L,
//            1F,
//            locationListener
//        )
//    }
//
//    private val locationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            // 위치 정보가 갱신되었을 때 호출됩니다.
//            val latitude = location.latitude
//            val longitude = location.longitude
//            // TODO: 위치 정보를 사용하는 코드 작성
//        }
//
//        override fun onProviderDisabled(provider: String) {
//            // 위치 정보 제공자가 비활성화 되었을 때 호출됩니다.
//        }
//
//        override fun onProviderEnabled(provider: String) {
//            // 위치 정보 제공자가 활성화 되었을 때 호출됩니다.
//        }
//
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
//            // 위치 정보 제공자의 상태가 변경되었을 때 호출됩니다.
//        }
//    }
//
//    override fun onBind(p0: Intent?): IBinder? {
//        replyMessenger.
//    }
//}