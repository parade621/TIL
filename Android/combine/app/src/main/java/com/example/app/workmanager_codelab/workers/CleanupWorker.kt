package com.example.app.workmanager_codelab.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.app.workmanager_codelab.OUTPUT_PATH
import java.io.File

class CleanupWorker(context: Context, params: WorkerParameters): Worker(context, params){
    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old tmeporaty files", applicationContext)
        sleep()

        return try{
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if(outputDirectory.exists()){
                val entries = outputDirectory.listFiles()
                if(entries != null){
                    for (entry in entries) {
                        val name = entry.name
                        if(name.isNotEmpty() && name.endsWith(".png")){
                            val deleted = entry.delete()
                            Log.i("TAG", "Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success()

        }catch(exception:Exception){
            exception.printStackTrace()
            Result.failure()
        }
    }
}