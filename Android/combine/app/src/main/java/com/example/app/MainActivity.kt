package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.camera_view.CameraActivity
import com.example.app.customdialog.CustomDialogActivity
import com.example.app.databinding.ActivityMainBinding
import com.example.app.databinding_ex.DataBindingActivity
import com.example.app.dialogs.DialogsActivity
import com.example.app.doit_ch14_batteryInfo.Main14Activity
import com.example.app.enhanced_todo.EnhancedToDoActivity
import com.example.app.flow_practice.FlowPracticeActivity
import com.example.app.fragmentlifecycletest.FLTActivity
import com.example.app.kakaonotification.KNActivity
import com.example.app.livedata_ex.LVEActivity
import com.example.app.nav_and_sharedviewmodel.ui.NSActivity
import com.example.app.notifications.NotiActivity
import com.example.app.retrofit_ex.ui.LottoActivity
import com.example.app.sharedpref_ex.SharedPrefActivity
import com.example.app.thread_timer.TTActivity
import com.example.app.viewpager2.VPActivity
import com.example.layoutex1.ui.LEActivity

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
            val intent = Intent(this, FlowPracticeActivity::class.java)
            startActivity(intent)
        }
        binding.btnSharedPref.setOnClickListener {
            val intent = Intent(this, SharedPrefActivity::class.java)
            startActivity(intent)
        }

        binding.enhancedTodo.setOnClickListener {
            val intent = Intent(this, EnhancedToDoActivity::class.java)
            startActivity(intent)
        }
    }
}