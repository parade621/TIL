package com.example.app.camera_view.gps

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.camera_view.gps.GpsData.isGoogleService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.internal.service.Common
import java.util.prefs.Preferences


object GpsData {
    var isGoogleService = false

    var latitude = 0.0
    var longitude = 0.0
    var accuracy = 0.0
    var lastUpdatedTime: Long = 0

    fun isServiceRun(context: Context): Boolean {
        if (isGoogleService) {
            if (isServiceRunning(context, GoogleService::class.java)) {
                return true
            }
        }
        return false
    }

    fun startGpsService(context: Context) {
        isGoogleService = useGoogleService(context)

        if (isGoogleService) {
            if (!isServiceRunning(context, GoogleService::class.java)) {
                val intent = Intent(context, GoogleService::class.java)
                ContextCompat.startForegroundService(context, intent)
            }
        }
    }

    fun stopGpsService(context: Context) {
        try {
            if (isServiceRunning(context, GoogleService::class.java)) {
                val intent = Intent(context, GoogleService::class.java)
                context.stopService(intent)
            }
        } catch (e: Exception) {
            //
        }
    }


    private fun useGoogleService(context: Context): Boolean {     // true googleplay , false 샤오미
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)

        return ConnectionResult.SUCCESS == status
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager =
            context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}
