package com.example.app.workmanager_codelab

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.app.R
import com.example.app.workmanager_codelab.workers.BlurWorker
import com.example.app.workmanager_codelab.workers.CleanupWorker
import com.example.app.workmanager_codelab.workers.SaveImageToFileWorker


class BlurViewModel(application: Application) : ViewModel() {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos : LiveData<List<WorkInfo>> = workManager.getWorkInfosByTagLiveData(
        TAG_OUTPUT)

    init {
        imageUri = getImageUri(application.applicationContext)
    }

    internal fun applyBlur(blurLevel: Int) {

        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        for (i in 0 until blurLevel) {
            val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()

            if (i == 0) {
                blurRequest.setInputData(createInputForUri())
            }
            continuation = continuation.then(blurRequest.build())
        }

        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT) // 작업 태그 지정 -> WorkInfo를 가져올 수 있게 된다.
            .build()

        continuation = continuation.then(save)

        continuation.enqueue()


        workManager.enqueue(OneTimeWorkRequest.from(BlurWorker::class.java))
    }

    internal fun cancelWork(){
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    private fun createInputForUri(): Data {
        val builder = Data.Builder()
        imageUri.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources

        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
            .build()

        return imageUri
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

    class BlurViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
                BlurViewModel(application) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
