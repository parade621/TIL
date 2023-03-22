package com.example.app.camera_view

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.AttributeSet
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.lifecycle.lifecycleScope
import com.example.app.databinding.CameraViewBinding
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Camera custom view
 */

class CameraView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    RelativeLayout(context, attrs, defStyle) {

    companion object {
        const val TAG = "CameraView"

        fun resizeBitmapImage(source: Bitmap, maxResolution: Int = 1500): Bitmap? {
            val width = source.width
            val height = source.height
            var newWidth = width
            var newHeight = height
            val rate: Float
            if (width > height) {
                if (maxResolution < width) {
                    rate = maxResolution / width.toFloat()
                    newHeight = (height * rate).toInt()
                    newWidth = maxResolution
                }
            } else {
                if (maxResolution < height) {
                    rate = maxResolution / height.toFloat()
                    newWidth = (width * rate).toInt()
                    newHeight = maxResolution
                }
            }
            return Bitmap.createScaledBitmap(source, newWidth, newHeight, true)
        }

    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    private var binding: CameraViewBinding

    private var cameraExecutor: ExecutorService

    init {
        binding = CameraViewBinding.inflate(LayoutInflater.from(context), this, true)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }


    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null


    fun binding(activity: AppCompatActivity) {
        activity.lifecycleScope.launch {
            cameraProvider = ProcessCameraProvider.getInstance(context).await()

            val cameraProvider = cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

            val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

            val rotation = try {
                binding.viewFinder.display.rotation
            } catch (e: Exception) {
                0
            }

            preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .build()

            imageCapture = Builder()
                .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .build()

            cameraProvider.unbindAll()

            if (camera != null) {
                camera!!.cameraInfo.cameraState.removeObservers(activity)
            }

            try {
                val useCaseGroup = UseCaseGroup.Builder()

                if (preview != null) {
                    useCaseGroup.addUseCase(preview!!)
                }

                if (imageCapture != null) {
                    useCaseGroup.addUseCase(imageCapture!!)
                }

                val viewPort = ViewPort.Builder(Rational(1, 1), rotation).build()

                useCaseGroup.setViewPort(viewPort)

                camera =
                    cameraProvider.bindToLifecycle(activity, cameraSelector, useCaseGroup.build())

                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }
    }


    suspend fun capture(): Bitmap? {
        return suspendCoroutine {
            if (imageCapture != null) {

                imageCapture!!.takePicture(cameraExecutor, object : OnImageCapturedCallback() {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        it.resume(null)
                    }

                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)

                        val rotation = image.imageInfo.rotationDegrees

                        Log.e(TAG, "onCaptureSuccess / rotation = $rotation")

                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)

                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                        val rotationBitmap = if (rotation != 0) {
                            val matrix = Matrix()
                            matrix.setRotate(
                                rotation.toFloat(),
                                bitmap.width.toFloat() / 2,
                                bitmap.height.toFloat() / 2
                            )

                            Bitmap.createBitmap(
                                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                            )
                        } else {
                            bitmap
                        }

                        image.close()

                        it.resume(resizeBitmapImage(rotationBitmap, 1500))
                    }
                })

            } else {
                Log.e(TAG, "imageCapture == null")
                it.resume(null)
            }
        }
    }

    fun onDestroyView() {
        cameraExecutor.shutdown()
    }

}

