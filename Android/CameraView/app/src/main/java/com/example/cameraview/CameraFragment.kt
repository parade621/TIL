package com.example.cameraview

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cameraview.databinding.FragmentCameraBinding
import java.util.concurrent.ExecutorService
import kotlin.system.exitProcess

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val parentActivity: MainActivity by lazy {
        requireActivity() as MainActivity
    }

    // Properties
    private var iamgeCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

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
    }

    private fun takePhoto() {}

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
            Log.d("View", "Im on a preview!!!!!!!")
            // 후방 카메라를 디폴트로 설정
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // 다시 바인딩 하기 전에, 자원을 언바인딩
                cameraProvider.unbindAll()

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
                    it?.finishAffinity()
                    exitProcess((0))
                }
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