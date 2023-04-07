package com.example.app.workmanager_codelab.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.app.R
import com.example.app.workmanager_codelab.KEY_IMAGE_URI

class BlurWorker(context: Context, params: WorkerParameters): Worker(context, params) {



    override fun doWork(): Result {
        val appContext = applicationContext

        val resourcesUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)
        sleep()

        return try {

            if(TextUtils.isEmpty(resourcesUri)){
                Log.e("TAG", "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourcesUri))
            )

            val output = blurBitmap(picture, appContext)

            // 임시파일에 비트맵을 write
            val outputUri = writeBitmapToFile(appContext, output)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        }catch(throwable: Throwable){
            Log.e("BlurWorker", "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }


}