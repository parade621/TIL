package com.example.app.doit_ch14_batteryInfo

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.databinding.ActivityMain14Binding

class Main14Activity : AppCompatActivity() {

    private val binding : ActivityMain14Binding by lazy{
        ActivityMain14Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){
            if(it.all{permission -> permission.value == true}){
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }else{
                Toast.makeText(binding.root.context, "권한이 거부됨", Toast.LENGTH_SHORT).show()
            }
        }

        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply{
            when(getIntExtra(BatteryManager.EXTRA_STATUS, -1)){
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when(getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)){
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources, R.drawable.usb
                            ))
                        }
                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources, R.drawable.ac
                            ))
                        }
                    }
                }
                else->{
                    binding.chargingResultView.text = "Not Plugged"
                }
            }
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100
            binding.percentResultView.text = "$batteryPct %"
        }
        binding.button.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if(ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                ) == PackageManager.PERMISSION_GRANTED
                ){
                    Log.e("Receiver Error?", "In Action?????")
                    val intent = Intent(this, MyReceiver::class.java)
                    sendBroadcast(intent)
                }else{
                    Log.e("Receiver Error", "Not In Action!!!!")
                        requestPermission()
                }
            }else{
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this@Main14Activity,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            100
        )
    }
}