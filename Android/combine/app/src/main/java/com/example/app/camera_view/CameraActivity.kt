package com.example.app.camera_view

import android.app.ProgressDialog.show
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.app.camera_view.util.ImageUtil
import com.example.app.camera_view.util.PermissionUtil
import com.example.app.R
import com.example.app.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService

class CameraActivity : AppCompatActivity() {

    private val binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }

    // Properties
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraAnimationListener: Animation.AnimationListener
    private var savedUri: Uri? = null

    // test
    private val savedUriList = mutableListOf<Uri?>()
    private lateinit var ImgViewList: Array<ImageView>
    private lateinit var requestGalleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setImgView()
        permissionCheck()
        setCameraAnumationListener()
        setClickListener()
        requestGalleryLauncher = getPhotoFromGallery()

        binding.shutter.setOnClickListener {
            takePhoto()
        }
        binding.gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }
    }

    private fun setImgView() {
        ImgViewList = arrayOf(
            binding.recentPhoto,
            binding.recentPhoto2,
            binding.recentPhoto3,
            binding.recentPhoto4,
        )
    }


    /**
     * 사진을 촬영(이미지 캡쳐)하는 기능을 수행하는 함수입니다.
     *
     */
    private fun takePhoto() {
        // 안드로이드 카메라 api2에서 제공하는 캡처 기능을 안정적으로 참조하기 위해 선언
        val imageCapture = imageCapture ?: return

        // 사진명으로 시간 데이터를 저장합니다.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
            .format(System.currentTimeMillis())

        // 사진의 메타데이터를 저장합니다.
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // 파일과 메타데이터가 담긴 출력 객체 생성
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // 촬영 진행
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(binding.root.context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(e: ImageCaptureException) {
                    Log.e(TAG, "사진 촬영에 실패했습니다 %{e.message}", e)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // 촬영한 사진의 uri를 받습니다.
                    savedUriList.add(outputFileResults.savedUri)
                    // savedUri = outputFileResults.savedUri

                    Toast.makeText(
                        binding.root.context,
                        "사진을 촬영하였습니다.", Toast.LENGTH_SHORT
                    ).show()
                    val shutterAnimation = AnimationUtils.loadAnimation(
                        binding.root.context,
                        R.anim.shutter_anim
                    )
                    shutterAnimation.setAnimationListener(cameraAnimationListener)
                    binding.frameLayoutShutter.apply {
                        animation = shutterAnimation
                        visibility = View.VISIBLE
                        startAnimation(shutterAnimation)
                    }
                }
            }
        )
    }

    private fun setCaptureImage() {
        // 촬영한 이미지를 미리보기 창에 쏴주기
        //binding.recentPhoto.setImageURI(savedUri)
        if (savedUriList.size >= 4) {
            for (i in 0..3) {
                ImgViewList[i].setImageURI(savedUriList[i])
            }

        } else {
            for (i in savedUriList.indices) {
                ImgViewList[i].setImageURI(savedUriList[i])
            }
        }
        setClickListener()

    }

    /**
     * 카메라를 실행하여 previewView에 화면을 띄웁니다.
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(binding.root.context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // set Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .build()

            // 후방 카메라를 디폴트로 설정
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // 다시 바인딩 하기 전에, 자원을 언바인딩
                cameraProvider.unbindAll()

                // 카메라에 use-case(촬영) 기능을 연결
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

                //  카메라 자원 할당
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview
                )
            } catch (e: Exception) {
                Log.e("Camera Fargement", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(binding.root.context))

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown() // 카메라 종료
    }

    private fun permissionCheck() {
        var permissionList =
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

        if (!PermissionUtil.checkPermission(binding.root.context, permissionList)) {
            PermissionUtil.requestPermission(this, permissionList)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "권한 승인")
            startCamera()
        } else {
            //
            Log.e(TAG, "권한 거절")
            onBackPressed()
        }
    }

    /**
     * 이미지 캡쳐가 진행되었다는 것을 알 수 있는 방법이 마땅하지 않아
     * 셔터 애니메이션을 추가하였습니다.
     * 애니메이션이 종료될 때, 이미지를 미리보기 창에 띄우기 위해 setCaptureImage()를 호출합니다.
     */
    private fun setCameraAnumationListener() {
        cameraAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.frameLayoutShutter.visibility = View.GONE
                setCaptureImage()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        }
    }

    /**
     * 사진 미리보기를 클릭하면, 큰 화면에서 볼 수 있도록 액티비티를 이동합니다.
     */
    private fun showPreview(uri: Uri) {
        Log.d("Show PreView Function", "Invoked!!!!!!!!!!!!")
        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra("uriInfo", uri.toString())
        startActivity(intent)
    }

    private fun getPhotoFromGallery() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        try {
            val option = BitmapFactory.Options()

            var inputStream = contentResolver.openInputStream(activityResult!!.data!!.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
            inputStream!!.close()
            inputStream = null

            // 갤러리에서 가져온 이미지가 회전되어있어, 90도로 회전해줍니다.
            val rotateBitmap = ImageUtil.rotateBitmap(bitmap!!, 90f)

            // Bitmap에서 Uri로 변환
            val imageUri = ImageUtil.getUriFromBitmap(this, rotateBitmap)

            savedUriList.add(imageUri)
            setCaptureImage()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setClickListener(){
        if (savedUriList.isNotEmpty()) {
            if (savedUriList.size < 4) {
                Log.e("join Here", "Confirmed.!!!!!!!")
                for (i in savedUriList.indices) {
                    ImgViewList[i].setOnClickListener {
                        Log.d("Preview touched", "ImgViewList[${i}] is touched")
                        showPreview(savedUriList[i]!!)
                    }
                }
            }else{
                for (i in ImgViewList.indices) {
                    ImgViewList[i].setOnClickListener {
                        Log.d("Preview touched", "ImgViewList[${i}] is touched")
                        showPreview(savedUriList[i]!!)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "Camera_App"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
