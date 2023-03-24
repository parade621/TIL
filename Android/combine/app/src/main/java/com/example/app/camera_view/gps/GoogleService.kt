package com.example.app.camera_view.gps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
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
        super.onStartCommand(intent, flags, startId)

        try {
            startForeground(NOTIFICATION_ID, createChannel().build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        createFusedProvider()

        return START_STICKY
    }


    private fun createChannel(): NotificationCompat.Builder {

        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            val channel = NotificationChannel(
                channelId,
                "Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

//        val intent = Intent(this, NotiClickEmptyActivity::class.java).apply {
//            action = NotiClickEmptyActivity.ACTION_OPEN
//        }
        val notificationIntent = Intent(this, CameraActivity::class.java)

//        val pendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(resources.getString(R.string.text_gps_service))
            .setContentText("Location is Running in the Background")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
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

