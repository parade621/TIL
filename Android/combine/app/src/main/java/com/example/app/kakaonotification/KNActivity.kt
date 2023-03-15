package com.example.app.kakaonotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.databinding.ActivityKnactivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class KNActivity : AppCompatActivity() {

    // permission request code
    private val REQUEST_NOTIFICATION_PERMISSION = 1

    private val binding: ActivityKnactivityBinding by lazy {
        ActivityKnactivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions() // Contract 객체 등록
        ) {
            if (it.all { permission -> permission.value == true }) { // 모든 권한을 허용한 경우
                noti()
            } else {
                Toast.makeText(this, "권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                requestPermission()
            }
        }

        binding.notificationButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    noti()
                } else {
                    // 허용을 요청할 권한을 객체에 담아서 luncher를 실행.
                    permissionLauncher.launch(
                        arrayOf(
                            "anmdroid.permission.POST_NOTIFICATIONS"
                        )
                    )
                }
            } else {
                noti()
            }
        }
    }

    private fun noti() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 오레오(Api 26)부터는 빌더를 만들 때 채널이라는 개념을 추가해줘야 한다.
            val channerlId = "one-channerl"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channerlId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "My Channel One Description"
            channel.setShowBadge(true) // 아이콘에 확인하지 않은 알람 갯수를 뱃지로 표시
            // 소리 설정
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            // 불빛 표시 여부
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            // 알림 진동 패턴
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channerlId)
        } else {
            builder = NotificationCompat.Builder(this)
        }

        // 알림 기본 정보
        builder.run {
            setSmallIcon(R.drawable.small)
            setWhen(System.currentTimeMillis())
            setContentTitle("박상현")
            setContentText("반가워요")
            setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
        }

        val KEY_TEXT_REPLY = "key_text_replay"
        var replyLabel = "답장"
        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
        val replyIntent = Intent(this, ReplyReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(
            this, 30, replyIntent, PendingIntent.FLAG_MUTABLE
        )

        builder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.send,
                "답장",
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()
        )
        manager.notify(11, builder.build())
    }

    // requestPermission을 통해 권한을 요청했을때 호출되는 콜백 메서드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("onRequestPermissionsResult", "InVoked!!!!")
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            Log.d("onRequestPermissionsResult", "want to check whether this gets invoked after one more rejection")
            if (grantResults.size > 0) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
//                        // 권한 거절시 실행되는 코드
//                        // Toast Massage
//                        Toast.makeText(this, "권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                        Log.d("onRequestPermissionsResult", "ok we got this point 3")
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)){
                            Log.d("onRequestPermissionsResult", "ok we got this point 4")
                            Toast.makeText(this, "권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                            requestPermission()
                        }else {
                            Log.d("onRequestPermissionsResult", "this part be executed upon the second rejection")

                            /**
                            권한 거부 처리
                            Android11(API 수준 30) 부터 사용자가 앱이 기기에 설치된 전체 기간 동안 특정 권한에 관해 거부를 두 번 이상 탭하면
                            앱에서 그 권한을 다시 요청하는 경우 사용자에게 시스템 권한 대화상자가 표시되지 않습니다. 이러한 사용자의 작업은 '다시 묻지 않음'을 의미한다.
                            사용자가 권한 요청을 두 번 이상 거부하면 영구 거부로 간주된다.
                             때문에 이 else문에서 snackbar 혹은 Toast 메세지를 사용자가 직접 권한 허용을 설정하도록 안내하는게 좋을듯 하다.
                             */

                        }
                    } else {
                        // 권한 허용시, 실행되는 코드
                        noti()
                    }
                }
            }
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this@KNActivity,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            REQUEST_NOTIFICATION_PERMISSION
        )
    }
}