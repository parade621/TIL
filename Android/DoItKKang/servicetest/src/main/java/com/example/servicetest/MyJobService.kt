package com.example.servicetest

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class MyJobService :JobService(){

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MYJobService........onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MYJobService........onDestroy()")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "MYJobService........onStart()")
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "MYJobService........onStop()")
        return false
    }

    companion object {
        private const val TAG = "TEST_Service"
    }

}