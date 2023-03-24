package com.example.app.flow_practice

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

// 퍼미션 체크
fun Context.hasLocationPermission(): Boolean{
    return ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}