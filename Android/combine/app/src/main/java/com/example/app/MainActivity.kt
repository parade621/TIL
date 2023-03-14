package com.example.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.app.customdialog.CustomDialogActivity
import com.example.app.databinding.ActivityMainBinding
import com.example.app.databinding.CustomDialogBinding
import com.example.app.databinding_ex.DataBindingActivity
import com.example.app.dialogs.DialogsActivity
import com.example.app.fragmentlifecycletest.FLTActivity
import com.example.app.kakaonotification.KNActivity
import com.example.app.livedata_ex.LVEActivity
import com.example.app.nav_and_sharedviewmodel.ui.NSActivity
import com.example.app.notifications.NotiActivity
import com.example.app.retrofit_ex.ui.LottoActivity
import com.example.app.thread_timer.TTActivity
import com.example.app.viewpager2.VPActivity
import com.example.layoutex1.ui.LEActivity

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var intent:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        binding.btnDatabindingBasic.setOnClickListener {
            intent = Intent(this, DataBindingActivity::class.java)
            startActivity(intent)
        }
        binding.btnDialogs.setOnClickListener {
            intent = Intent(this, DialogsActivity::class.java)
            startActivity(intent)
        }
        binding.btnCustomDialogs.setOnClickListener {
            intent = Intent(this, CustomDialogActivity::class.java)
            startActivity(intent)
        }
        binding.btnFragmentLifeCycle.setOnClickListener {
            intent = Intent(this, FLTActivity::class.java)
            startActivity(intent)
        }
        binding.btnNoti.setOnClickListener {
            intent = Intent(this, NotiActivity::class.java)
            startActivity(intent)
        }
        binding.btnKN.setOnClickListener {
            intent = Intent(this, KNActivity::class.java)
            startActivity(intent)
        }
        binding.btnLayoutTest.setOnClickListener {
            intent = Intent(this, LEActivity::class.java)
            startActivity(intent)
        }
        binding.btnRetrofit.setOnClickListener {
            intent = Intent(this, LottoActivity::class.java)
            startActivity(intent)
        }
        binding.btnNNS.setOnClickListener {
            intent = Intent(this, NSActivity::class.java)
            startActivity(intent)
        }
        binding.btnThread.setOnClickListener {
            intent = Intent(this, TTActivity::class.java)
            startActivity(intent)
        }
        binding.btnViewPager2.setOnClickListener {
            intent = Intent(this, VPActivity::class.java)
            startActivity(intent)
        }
        binding.btnTmp2.setOnClickListener {
            intent = Intent(this, LVEActivity::class.java)
            startActivity(intent)
        }
    }
}