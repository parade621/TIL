package com.example.app.workmanager_location

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.app.Utils.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class LocationWorker(val context: Context, params: WorkerParameters): Worker(context, params) {

    companion object{
        // 주기적 요청으로 워커 생성
        fun run(): LiveData<WorkInfo>{
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

        LocationInteractor.currentLocation?.let{
            CoroutineScope(Dispatchers.Main).launch {
                val locationAsString = "Location: ${it.latitude}, ${it?.longitude}"
                Toast.makeText(applicationContext, locationAsString, Toast.LENGTH_SHORT).show()

                DataStoreManager.setMyLocation(locationAsString)

                Log.d("일함", "잘돌아가용?")
            }
        }

        return if(INTERVAL < MINUTE_IN_MS){
            Result.retry()
        }else{
            Result.success()
        }
    }
}