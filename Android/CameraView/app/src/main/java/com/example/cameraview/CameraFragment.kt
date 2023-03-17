package com.example.cameraview

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.content.ContextWrapper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cameraview.databinding.FragmentCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import kotlin.system.exitProcess

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val parentActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    // Properties
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraAnimationListener: AnimationListener
    private var savedUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionCheck()
        setCameraAnumationListener()
        outputDirectory = getOutputDirectory()

        binding.shutter.setOnClickListener {
            takePhoto()
        }
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
            .Builder(requireActivity().contentResolver,
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
                    savedUri = outputFileResults.savedUri

                    Toast.makeText(binding.root.context,
                    "사진을 촬영하였습니다.", Toast.LENGTH_SHORT).show()
                    val shutterAnimation = AnimationUtils.loadAnimation(binding.root.context,
                    R.anim.shuttur_anim)
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
        binding.recentPhoto.setImageURI(savedUri)
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
                    viewLifecycleOwner, cameraSelector,preview,imageCapture)

                //  카메라 자원 할당
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (e: Exception) {
                Log.e("Camera Fargement", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(binding.root.context))

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        cameraExecutor.shutdown() // 카메라 종료
    }

    private fun permissionCheck(){
        var permissionList=
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

        if(!PermissionUtil.checkPermission(binding.root.context, permissionList)){
            PermissionUtil.requestPermission(requireActivity(), permissionList)
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
            Log.e(TAG, "권한 거절")
            requireActivity().finish()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
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