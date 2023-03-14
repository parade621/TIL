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
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.databinding.ActivityKnactivityBinding

class KNActivity : AppCompatActivity() {

    private val binding : ActivityKnactivityBinding by lazy{
        ActivityKnactivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){
            if (it.all{permission -> permission.value == true}){
                noti()
            }else{
                Toast.makeText(this, "permission denied....", Toast.LENGTH_SHORT).show()
            }
        }

        binding.notificationButton.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                if(ContextCompat.checkSelfPermission(
                        this,
                        "android.permission.POST_NOTIFICATIONS"
                    )== PackageManager.PERMISSION_GRANTED){
                    noti()
                }else{
                    permissionLauncher.launch(
                        arrayOf(
                            "anmdroid.permission.POST_NOTIFICATIONS"
                        )
                    )
                }
            }else{
                noti()
            }
        }
    }

    private fun noti() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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
            val audioAttributes= AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            // 불빛 표시 여부
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            // 알림 진동 패턴
            channel.vibrationPattern = longArrayOf(100,200,100,200)

            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channerlId)
        }else{
            builder = NotificationCompat.Builder(this)
        }

        // 알림 기본 정보
        builder.run{
            setSmallIcon(R.drawable.small)
            setWhen(System.currentTimeMillis())
            setContentTitle("박상현")
            setContentText("반가워요")
            setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.big))
        }

        val KEY_TEXT_REPLY = "key_text_replay"
        var replyLabel = "답장"
        val remoteInput : RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run{
            setLabel(replyLabel)
            build()
        }
        val replyIntent = Intent(this,ReplyReceiver::class.java)
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
        manager.notify(11,builder.build())
    }
}