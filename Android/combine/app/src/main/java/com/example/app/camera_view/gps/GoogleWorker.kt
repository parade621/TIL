package com.example.app.camera_view.gps

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.app.R
import com.google.android.gms.common.internal.service.Common
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences

class GoogleWorker(private val context: Context, private val reference: String) {
    private val TAG = "GoogleWorker"

    private val googleLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var MIN_TIME_BW_UPDATES: Long = (1000 * 10).toLong()
    private var MIN_FAST_INTERVAL_UPDATES: Long = (1000 * 10).toLong()
    private var MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 0

    init {
        if (reference == "time_fused") {
            MIN_TIME_BW_UPDATES = (1000 * 60 * 3).toLong()
            MIN_FAST_INTERVAL_UPDATES = (1000 * 60 * 3).toLong()
            MIN_DISTANCE_CHANGE_FOR_UPDATES = 0
        } else if (reference == "distance_fused") {
            MIN_TIME_BW_UPDATES = (1000 * 60).toLong()
            MIN_FAST_INTERVAL_UPDATES = (1000 * 60).toLong()
            MIN_DISTANCE_CHANGE_FOR_UPDATES = 500
        }

        GpsData.latitude = 0.0
        GpsData.longitude = 0.0
    }

    fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        googleLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.e(TAG, "addOnSuccessListener")
                GpsData.latitude = location.latitude
                GpsData.longitude = location.longitude
                GpsData.accuracy = location.accuracy.toDouble()
            }
        }

//        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
//            interval = MIN_TIME_BW_UPDATES
//            fastestInterval = MIN_FAST_INTERVAL_UPDATES
//            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
//            smallestDisplacement = MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat()
//        }
        // deprecated
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            MIN_TIME_BW_UPDATES
        ).build()


        googleLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.locations[0] != null) {
                Log.e(TAG, "LocationCallback")

                GpsData.latitude = locationResult.locations[0].latitude
                GpsData.longitude = locationResult.locations[0].longitude
                GpsData.lastUpdatedTime = System.currentTimeMillis()

                val dateFormat = SimpleDateFormat("HH", Locale.getDefault())
                val nowHour = dateFormat.format(Date())
            }
        }
    }

    fun removeLocationUpdates() {
        googleLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}