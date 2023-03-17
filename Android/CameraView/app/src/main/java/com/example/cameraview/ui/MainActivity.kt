package com.example.cameraview.ui

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.core.content.ContextCompat.startActivity
import com.example.cameraview.R
import com.example.cameraview.databinding.ActivityMainBinding
import com.example.cameraview.util.PermissionUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import com.example.cameraview.util.ImageUtil

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Properties
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraAnimationListener: Animation.AnimationListener
    private var savedUri : Uri? = null

    // test
    private val savedUriList = mutableListOf<Uri?>()
    private lateinit var ImgViewList:Array<ImageView>
    private lateinit var requestGalleryLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setImgView()
        permissionCheck()
        setCameraAnumationListener()
        outputDirectory = getOutputDirectory()
        requestGalleryLauncher = getPhotoFromGallery()

        binding.shutter.setOnClickListener {
            takePhoto()
        }
        if (savedUriList.isNullOrEmpty()) {
            binding.recentPhoto.setOnClickListener {
                showPreview(savedUriList[0]!!)
            }
            binding.recentPhoto2.setOnClickListener {
                showPreview(savedUriList[1]!!)
            }
            binding.recentPhoto3.setOnClickListener {
                showPreview(savedUriList[2]!!)
            }
            binding.recentPhoto4.setOnClickListener {
                showPreview(savedUriList[3]!!)
            }
        }
        binding.gallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type="image/*"
            requestGalleryLauncher.launch(intent)
        }
    }

    private fun setImgView(){
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
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // 파일과 메타데이터가 담긴 출력 객체 생성
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // 촬영 진행
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(binding.root.context),
            object : ImageCapture.OnImageSavedCallback{

                override fun onError(e: ImageCaptureException){
                    Log.e(TAG, "사진 촬영에 실패했습니다 %{e.message}",e)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // 촬영한 사진의 uri를 받습니다.
                    savedUriList.add(outputFileResults.savedUri)
                    // savedUri = outputFileResults.savedUri

                    Toast.makeText(binding.root.context,
                        "사진을 촬영하였습니다.", Toast.LENGTH_SHORT).show()
                    val shutterAnimation = AnimationUtils.loadAnimation(binding.root.context,
                        R.anim.shuttur_anim
                    )
                    shutterAnimation.setAnimationListener(cameraAnimationListener)
                    binding.frameLayoutShutter.apply{
                        animation = shutterAnimation
                        visibility = View.VISIBLE
                        startAnimation(shutterAnimation)
                    }
                }
            }
        )
    }
    private fun setCaptureImage(){
        // 촬영한 이미지를 미리보기 창에 쏴주기
        //binding.recentPhoto.setImageURI(savedUri)
        if (savedUriList.size >=4){
            for (i in 0..3){
                ImgViewList[i].setImageURI(savedUriList[i])
            }

        }else{
            for (i in savedUriList.indices){
                ImgViewList[i].setImageURI(savedUriList[i])
            }
        }

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
                    this, cameraSelector,preview,imageCapture)

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

    private fun permissionCheck(){
        var permissionList=
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

        if(!PermissionUtil.checkPermission(binding.root.context, permissionList)){
            PermissionUtil.requestPermission(this, permissionList)
        }else{
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
            PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "권한 승인")
            startCamera()
        }
        else{
            //
            Log.e(TAG, "권한 거절")
            onBackPressed()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun setCameraAnumationListener(){
        cameraAnimationListener = object: Animation.AnimationListener{
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

    private fun showPreview(uri:Uri){
        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra("uriInfo",uri.toString())
        startActivity(intent)
    }

    private fun getPhotoFromGallery() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){activityResult->
        try {

            val option = BitmapFactory.Options()

            var inputStream = contentResolver.openInputStream(activityResult!!.data!!.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
            inputStream!!.close()
            inputStream = null

            val rotateBitmap = ImageUtil.rotateBitmap(bitmap!!, 90f)

            val imageUri = ImageUtil.getUriFromBitmap(this, rotateBitmap)
//            val imageUri = contentResolver.insert(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                ContentValues().apply {
//                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                }
//            )
//            // Uri에 Bitmap을 저장
//            imageUri?.let{ uri->
//                contentResolver.openOutputStream(uri)?.use{outputStream ->
//                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                }
//            }



            savedUriList.add(imageUri)
            binding.recentPhoto.setImageURI(imageUri)
        }catch(e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
//        private const val REQUEST_CODE_PERMISSIONS = 10
//        private val REQUIRED_PERMISSIONS =
//            arrayOf(
//                android.Manifest.permission.CAMERA,
//            )
    }

}