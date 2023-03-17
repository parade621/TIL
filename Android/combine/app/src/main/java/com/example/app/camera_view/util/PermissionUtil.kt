package com.example.app.camera_view.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil{
    fun checkPermission(context: Context, permissionList: Array<String>): Boolean{
        for (i: Int in permissionList.indices){
            if(ContextCompat.checkSelfPermission(
                    context,
                    permissionList[i]
                ) == PackageManager.PERMISSION_DENIED
            ){
                return false
            }
        }
        return true
    }
    fun requestPermission(activity: Activity, permissionList: Array<String>){
        ActivityCompat.requestPermissions(
            activity,
            permissionList,
            10
        )
    }
}