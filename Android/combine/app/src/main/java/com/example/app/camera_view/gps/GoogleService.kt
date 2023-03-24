package com.example.app.camera_view.gps

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.app.R
import com.example.app.camera_view.CameraActivity


class GoogleService : Service() {

    companion object {
        const val TAG = "GoogleService"
        const val NOTIFICATION_ID = 1
        const val channelId = "GPS_Fused_Provider"
    }

    private var googleAccessWorker: GoogleWorker? = null
    private var googleAccessDistanceWorker: GoogleWorker? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")

        initializeNotification()

        createFusedProvider()
        return START_NOT_STICKY
    }

    private fun initializeNotification() {
        val builder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText("GPS Service")
                    .setBigContentTitle(null)
                    .setSummaryText("위치 서비스 동작중")
            )
            .setContentText(null)
            .setContentTitle(null)
            .setOngoing(true)
            .setWhen(0)
            .setShowWhen(false)

        val notificationIntent = Intent(this, CameraActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        builder.setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    "1",
                    "Foreground Service",
                    NotificationManager.IMPORTANCE_NONE
                )
            )
        }
        val notification = builder.build()
        startForeground(1, notification)
    }

    private fun createFusedProvider() {
        googleAccessWorker = GoogleWorker(this, "time_fused")
        googleAccessDistanceWorker = GoogleWorker(this, "distance_fused")

        googleAccessWorker?.startLocationUpdates()
        googleAccessDistanceWorker?.startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")

        try {
            googleAccessWorker?.removeLocationUpdates()
            googleAccessDistanceWorker?.removeLocationUpdates()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        GpsData.stopGpsService(this)
    }
}

