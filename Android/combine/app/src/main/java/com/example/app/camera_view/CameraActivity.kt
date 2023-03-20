package com.example.app.camera_view

import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
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
    private lateinit var cameraAnimationListener: Animation.AnimationListener
    private var savedUri: Uri? = null

    // test
    private val savedUriList = mutableListOf<Uri?>()
    private lateinit var imgViewList: Array<ImageView>
    private lateinit var requestGalleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setImgView()
        permissionCheck()
        setCameraAnumationListener()
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
        imgViewList = arrayOf(
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
    @SuppressLint("RestrictedApi")
    private fun takePhoto() {
        // 안드로이드 카메라 api2에서 제공하는 캡처 기능을 안정적으로 참조하기 위해 선언
        val imageCapture = imageCapture ?: return

        val name = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())}.jpg"

        // 이미지 파일 저장 경로 설정
        val outputDirectory = File(this.getExternalFilesDir(null), "captured_images")
        if (!outputDirectory.exists()) outputDirectory.mkdirs()
        val photoFile = File(outputDirectory, name)

        // 이미지 캡처 옵션 설정
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            photoFile
        ).build()



        // 촬영 진행
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this@CameraActivity),
            object : ImageCapture.OnImageSavedCallback {

                override fun onError(e: ImageCaptureException) {
                    Log.e(TAG, "사진 촬영에 실패했습니다 %{e.message}", e)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // 촬영한 사진의 uri를 받습니다.
                    savedUriList.add(outputFileResults.savedUri)
                    // savedUri = outputFileResults.savedUri

                    val fileUri = Uri.fromFile(photoFile)

                    // 미디어 캐시 삭제
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri))

                    MediaScannerConnection.scanFile(
                        applicationContext,
                        arrayOf(photoFile.absolutePath),
                        null,
                        object: MediaScannerConnection.OnScanCompletedListener{
                            override fun onScanCompleted(p0: String?, p1: Uri?) {
                                Log.d("Scanned", " 이거 확인하싶시오: $p0 , $p1")
                                Log.d("Scanned", "절대경로 맞음? ${photoFile.absolutePath}")
                            }
                        }
                    )

                    Toast.makeText(
                        binding.root.context,
                        "사진을 촬영하였습니다.", Toast.LENGTH_SHORT
                    ).show()

                    // 촬영 후, 셔터 애니메이션 실행
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
                    setCaptureImage()
                }
            }
        )
    }

    /**
     * [setCaptureImage] 함수
     * imgViewList의 크기만큼 for문을 돌면서 savedUriList에 저장된 uri를 각각의 이미지뷰에 설정합니다.
     * savedUriList에 저장된 이미지 uri가 ImgViewList의 크기보다 작은 경우, 아무런 동작도 수행하지 않도록 하여, 이미지 뷰를 빈칸으로 유지합니다.
     * 마지막으로, setImageClickListener() 메서드를 호출하여, 클릭 이벤트를 활성화합니다.
     */
    private fun setCaptureImage(){
        for (i in imgViewList.indices){
            if (i < savedUriList.size){
                imgViewList[i].setImageURI(savedUriList[i])
            }
        }
    setImageClickListener()
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

    override fun onStop() {
        super.onStop()
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

    /**
     * savedUirList가 비어있지 않고, 그 크기가 imgViewList의 전체 크기보다 작다면, for문을 돌면서 imgViewList에 대응하는 이미지뷰를 클릭하면,
     * showPreview()함수를 호출하는 클릭 리스너를 등록합니다.
     * savedUriList의 크기가 4이상인 경우에는 for 문을 imgViewList의 크기만큼 돌면서 위와 같은 기능을 수행합니다.
     * 만약 imgViewList에 대응하는 이미지뷰가 savedUriList에 없는 경우, 클릭 리스너를 등록하지 않습니다.(null)
     */
    private fun setImageClickListener(){
        val previewClickListener = View.OnClickListener { view->
            val position = view.tag as Int
            showPreview(savedUriList[position]!!)
        }
        for (i in imgViewList.indices){
            val view = imgViewList[i]
            if(savedUriList.isNotEmpty() && i < savedUriList.size){
                view.tag = i // 뷰 태그를 설정하여 Uir에 대한 정보를 저장해 둡니다.
                view.setOnClickListener(previewClickListener)
            }else{
                view.setOnClickListener(null)
            }
        }
    }

    companion object {
        private const val TAG = "Camera_App"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
