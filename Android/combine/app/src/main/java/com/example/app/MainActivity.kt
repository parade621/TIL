package com.example.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.app.camera_view.CameraActivity
import com.example.app.camera_view.gps.GpsData
import com.example.app.camera_view.util.hasPermissions
import com.example.app.customdialog.CustomDialogActivity
import com.example.app.databinding.ActivityMainBinding
import com.example.app.databinding_ex.DataBindingActivity
import com.example.app.dialogs.DialogsActivity
import com.example.app.doit_ch14_batteryInfo.Main14Activity
import com.example.app.enhanced_todo.EnhancedToDoActivity
import com.example.app.fragmentlifecycletest.FLTActivity
import com.example.app.kakaonotification.KNActivity
import com.example.app.livedata_ex.LVEActivity
import com.example.app.nav_and_sharedviewmodel.ui.NSActivity
import com.example.app.notifications.NotiActivity
import com.example.app.retrofit_ex.ui.LottoActivity
import com.example.app.shared_prefs_singleton.ui.SharedPrefsActivity
import com.example.app.sharedpref_ex.SharedPrefActivity
import com.example.app.thread_timer.TTActivity
import com.example.app.viewpager2.VPActivity
import com.example.app.webview1.WebView1Activity
import com.example.app.webview2.WebView2Activity
import com.example.app.workmanager_codelab.BlurActivity
import com.example.app.workmanager_location.LocationWorkerActivity
import com.example.layoutex1.ui.LEActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkRunTimePermission()
    }

    override fun onResume() {
        super.onResume()
        binding.btnDatabindingBasic.setOnClickListener {
            val intent = Intent(this, DataBindingActivity::class.java)
            startActivity(intent)
        }
        binding.btnDialogs.setOnClickListener {
            val intent = Intent(this, DialogsActivity::class.java)
            startActivity(intent)
        }
        binding.btnCustomDialogs.setOnClickListener {
            val intent = Intent(this, CustomDialogActivity::class.java)
            startActivity(intent)
        }
        binding.btnFragmentLifeCycle.setOnClickListener {
            val intent = Intent(this, FLTActivity::class.java)
            startActivity(intent)
        }
        binding.btnNoti.setOnClickListener {
            val intent = Intent(this, NotiActivity::class.java)
            startActivity(intent)
        }
        binding.btnKN.setOnClickListener {
            val intent = Intent(this, KNActivity::class.java)
            startActivity(intent)
        }
        binding.btnLayoutTest.setOnClickListener {
            val intent = Intent(this, LEActivity::class.java)
            startActivity(intent)
        }
        binding.btnRetrofit.setOnClickListener {
            val intent = Intent(this, LottoActivity::class.java)
            startActivity(intent)
        }
        binding.btnNNS.setOnClickListener {
            val intent = Intent(this, NSActivity::class.java)
            startActivity(intent)
        }
        binding.btnThread.setOnClickListener {
            val intent = Intent(this, TTActivity::class.java)
            startActivity(intent)
        }
        binding.btnViewPager2.setOnClickListener {
            val intent = Intent(this, VPActivity::class.java)
            startActivity(intent)
        }
        binding.btnTmp2.setOnClickListener {
            val intent = Intent(this, LVEActivity::class.java)
            startActivity(intent)
        }
        binding.btnTodo.setOnClickListener {
//            val intent = Intent(this, Main13Activity::class.java)
//            startActivity(intent)
        }
        binding.btnBattertInfo.setOnClickListener {
            val intent = Intent(this, Main14Activity::class.java)
            startActivity(intent)
        }
        binding.btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
        binding.btnFlowPractice.setOnClickListener {
//            val intent = Intent(this, FlowPracticeActivity::class.java)
//            startActivity(intent)
        }
        binding.btnSharedPref.setOnClickListener {
            val intent = Intent(this, SharedPrefActivity::class.java)
            startActivity(intent)
        }
        binding.enhancedTodo.setOnClickListener {
            val intent = Intent(this, EnhancedToDoActivity::class.java)
            startActivity(intent)
        }
        binding.sharedPrefsSingleton.setOnClickListener {
            val intent = Intent(this, SharedPrefsActivity::class.java)
            startActivity(intent)
        }
        binding.workmanager.setOnClickListener {
            val intent = Intent(this, BlurActivity::class.java)
            startActivity(intent)
        }
        binding.locationWorkmanager.setOnClickListener {
            val intent = Intent(this, LocationWorkerActivity::class.java)
            startActivity(intent)
        }
        binding.webview1.setOnClickListener {
            val intent = Intent(this, WebView1Activity::class.java)
            startActivity(intent)
        }
        binding.webview2.setOnClickListener {
            val intent = Intent(this, WebView2Activity::class.java)
            startActivity(intent)
        }
    }


    private fun checkRunTimePermission() {
        if (binding.root.context.hasPermissions()) {
            // 이미 권한이 허용됨
            Log.d(TAG, "checkRunTimePermission : 권한 이미 허용됨")
        } else {
            // 권한 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 사용자가 권한 거부를 한 번 이상 한 경우 snackbar 실행
                Snackbar.make(
                    binding.root,
                    "권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해주세요.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("확인") {
                    // 설정 화면으로 이동
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }.show()
            } else {
                // 권한 요청 팝업 출력
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허용됨
                    Log.d(TAG, "onRequestPermissionsResult : 권한 허용됨")
                } else {
                    // 권한 거부됨
                    Log.d(TAG, "onRequestPermissionsResult : 권한 거부됨")
                    checkRunTimePermission()
                }
                return
            }
        }
    }

    companion object {
        private const val TAG = "example.app"
        private const val PERMISSIONS_REQUEST_CODE = 100
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_IMAGES
        )
    }

}