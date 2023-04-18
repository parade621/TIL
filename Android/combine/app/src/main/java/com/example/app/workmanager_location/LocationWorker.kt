package com.example.app.workmanager_location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.app.utils.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class LocationWorker(val ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    companion object {
        // 주기적 요청으로 워커 생성
        fun run(): LiveData<WorkInfo> {
            val work = PeriodicWorkRequestBuilder<LocationWorker>(10, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                WORKER_ID,
                ExistingPeriodicWorkPolicy.REPLACE,
                work
            )
            return WorkManager.getInstance().getWorkInfoByIdLiveData(work.id)
        }
    }

    override fun doWork(): Result {
        Log.d("TAG", "-----------LocationWork")

        LocationInteractor.currentLocation?.let {
            CoroutineScope(Dispatchers.Main).launch {
                val locationAsString = "Location: ${it.latitude}, ${it.longitude}"
                val loca = getCurrentAddress(it.latitude, it.longitude)
                Toast.makeText(ctx, loca, Toast.LENGTH_SHORT).show()
                DataStoreManager.setMyLocation(loca)

            }
        }

        return if (INTERVAL < MINUTE_IN_MS) {
            Result.retry()
        } else {
            Result.success()
        }
    }
    fun getCurrentAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(ctx, Locale.getDefault())
        var addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            // 네트워크 문제
            Toast.makeText(ctx, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(ctx, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        if (addresses == null || addresses.isEmpty()) {
            return "주소 미발견"
        }

        val address: Address = addresses[0]
        return address.getAddressLine(0).toString() + "\n"
    }
}