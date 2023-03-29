//package com.example.app.flow_practice
//
//import android.location.Location
//import kotlinx.coroutines.flow.Flow
//
//interface LocationClient {
//    fun getLocationUpdates(interval: Long) : Flow<Location>
//
//    class LocationException(messege: String): Exception()
//}