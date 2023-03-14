package com.example.app.dialogs

import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.app.R
import com.example.app.databinding.ActivityDialogsBinding

class DialogsActivity : AppCompatActivity() {

    private val MY_PERMISSION_ACCESS_ALL = 100
    private val binding : ActivityDialogsBinding by lazy{
        ActivityDialogsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // 앱 실행시, 권한이 부여되지 않으면 요청하는 것
        // 아래 예제는 위치 정보 엑세스를 요청하는 코드이다.
        if (ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            var permissions = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_ACCESS_ALL)
        }

    }

    // 아래 코드는 사용자가 권한 거부 시, 앱을 종료하도록 처리하는 코드이다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_ACCESS_ALL){
            if(grantResults.size > 0){
                for (grant in grantResults){
                    Toast.makeText(this.applicationContext, "앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                    if(grant != PackageManager.PERMISSION_GRANTED) System.exit(0)
                }
            }
        }
    }

    // 뒤로가기 두번을 누르면 앱이 종료되게 하겠다.
    // 뒤로가기 두번을 누르면 앱이 종료되게 하겠다.
    override fun onResume() {
        super.onResume()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showToast()
            }
        })
    }

    // 뒤로가기 두번을 누르면 앱이 종료되는 기능.
    @RequiresApi(value = 30)
    private fun showToast(){
        val toast = Toast.makeText(binding.root.context, "종료하려면 한 번 더 누르세요.", Toast.LENGTH_SHORT)
        toast.addCallback(
            object : Toast.Callback(){
                override fun onToastHidden(){
                    super.onToastHidden()
                    Log.d("FirstFragment", "Toast Massege Hidden")
                }

                override fun onToastShown() {
                    super.onToastShown()
                    Log.d("FirstFragment", "Toast Massege Shown")
                    onBackPressedDispatcher.addCallback(this@DialogsActivity, object : OnBackPressedCallback(true){
                        override fun handleOnBackPressed() {
                            System.exit(0)
                        }
                    })
                }
            }
        )
        toast.show()
    }


    // 알림 빌더 작성
    fun alertBuilder(){
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder : NotificationCompat.Builder

    }
}