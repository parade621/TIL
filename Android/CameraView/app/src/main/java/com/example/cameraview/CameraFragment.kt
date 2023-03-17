package com.example.cameraview

import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraAnimationListener: AnimationListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        setCameraAnumationListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reqeust camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.shutter.setOnClickListener {
            takePhoto()
        }
    }

    private fun takePhoto() {
        // 안드로이드 카메라 api2에서 제공하는 캡처 기능을 안정적으로 참조하기 위해 선언
        val imageCapture = imageCapture ?: return

        // 사진이 저장될 객체의 미디어 데이터를 생성합니다.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply{
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        // 메타데이터를 포함하는 객체를 생성합니다.
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(requireActivity().contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // 촬영 후 실행될 이미지 캡쳐 리스너
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(binding.root.context),
            object : ImageCapture.OnImageSavedCallback{
                override fun onError(e: ImageCaptureException){
                    Log.e(TAG, "사진 촬영에 실패했습니다 %{e.message}",e)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
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

    /**
     * 카메라를 실행하여 previewView에 쏴줍니다.
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

    // 문제 있음.
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }.also{
        Log.d("퍼미션 체크", "${it}")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                // 권한을 허용하면 카메라를 실행시킵니다.
                Log.d("Permission Chekc!!!!!!! ", "WE GOT PERMISSIONS!!!!!!!")
                startCamera()
            } else {
                // 권한 거부시 종료시킵니다.
                // 설정으로 이동하는 방식을 추후에 구현하겠습니다.
                Toast.makeText(
                    binding.root.context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                parentActivity.let {
                    it.finishAffinity()
                    exitProcess((0))
                }
            }
        }
    }

    private fun setCameraAnumationListener(){
        cameraAnimationListener = object: Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.frameLayoutShutter.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            arrayOf(
                android.Manifest.permission.CAMERA,
            )
    }

}