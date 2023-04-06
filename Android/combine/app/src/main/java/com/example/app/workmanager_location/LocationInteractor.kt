package com.example.app.workmanager_location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

/**
 * Google 위치 서비스 API를 사용하면 프레임워크 위치 API보다 더 정확한 위치 정보를 제공하면서, 배터리 소모가 더 적다.
 * 전력 최적화가 되어있으며, 정확한 위치 정보를 제공하는 PRIORITY_BALANCED_POWER_ACCURACY를 사용한다.
 */

@SuppressLint("MissingPermission")
class LocationInteractor(val context: Context) {

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        var currentLocation: Location? = null
    }

    fun init() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(INTERVAL)
        ).build()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("TAG", "Permission not granted.")
            return
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation = locationResult.lastLocation
            }
        }


        fusedLocationProviderClient.apply {
            requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
            lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLocation = location
                }
            }
        }
    }
}