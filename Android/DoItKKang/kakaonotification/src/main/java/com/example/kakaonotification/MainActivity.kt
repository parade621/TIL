package com.example.kakaonotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.getSystem
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
import com.example.kakaonotification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
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
                )==PackageManager.PERMISSION_GRANTED){
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

        val builder:NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // ?????????(Api 26)????????? ????????? ?????? ??? ??????????????? ????????? ??????????????? ??????.
            val channerlId = "one-channerl"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channerlId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "My Channel One Description"
            channel.setShowBadge(true) // ???????????? ???????????? ?????? ?????? ????????? ????????? ??????
            // ?????? ??????
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes= AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            // ?????? ?????? ??????
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            // ?????? ?????? ??????
            channel.vibrationPattern = longArrayOf(100,200,100,200)

            // ????????? NotificationManager??? ??????
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channerlId)
        }else{
            builder = NotificationCompat.Builder(this)
        }

        // ?????? ?????? ??????
        builder.run{
            setSmallIcon(R.drawable.small)
            setWhen(System.currentTimeMillis())
            setContentTitle("?????????")
            setContentText("????????????")
            setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.big))
        }

        val KEY_TEXT_REPLY = "key_text_replay"
        var replyLabel = "??????"
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
                "??????",
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()
        )

        manager.notify(11,builder.build())
    }
}