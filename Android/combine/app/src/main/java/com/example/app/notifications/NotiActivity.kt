package com.example.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.app.R
import kotlin.concurrent.thread

class NotiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noti)


        // Do it 안드로이드 300페이지 실습 예제 코드: 알림 빌더 작성
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

        // 알림 객체 설정(위에서 부터 스몰 아이콘, 발생 시각, 제목, 내용)
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("Content Title")
        builder.setContentText("Content Massage")

        // 알림 발생
        //manager.notify(11, builder.build())
        /**
         * 위 코드에서 첫 번째 매개변수는 알림을 식별하는데 사용하는 숫자로, 코드에서 알림을 취소할 때 사용
         * cancel()을 사용하며, 아래와 같다.(주석처리해 둠)
         */
         manager.cancel(11)

        // 알림을 스와이프나 터치로 취소할 수 있지만, 이것 또한 다음 코드로 방지 가능
        // builder.setAutoCancel(false)
        // 위 코드는 터치로 알림이 사라지는 것을 방지
        // builder.setOngoing(true)
        // 위 코드는 스와이프로 알림이 사라지는 것을 방지.
        // 이 경우, 사용자가 알림을 취소하지 못하며, 코드에서 cancel()로 취소해야한다.

//        // 알림 객체에 액티비티 실행 정보 등록
//        val intent = Intent(this, MainActivity::class.java)
//        val pendingIntent =
//            PendingIntent.getActivity(this, 11, intent, PendingIntent.FLAG_IMMUTABLE)
//        builder.setContentIntent(pendingIntent) // 터치 이벤트 등록
//
//        //manager.notify(11, builder.build())
//
//        // 액션 등록하기
//        val actionIntent = Intent(this, MainActivity::class.java)
//        val actionPendingIntent = PendingIntent.getBroadcast(this, 20, actionIntent, PendingIntent.FLAG_IMMUTABLE)
//        builder.addAction(
//            NotificationCompat.Action.Builder(
//                android.R.drawable.stat_notify_more,
//                "Action",
//                actionPendingIntent
//            ).build()
//        )

        //manager.notify(11, builder.build())

//        // 원격 입력
//        val KEY_TEXT_REPLY = "key_text_reply"
//        var replyLabel: String = "답장"
//        var remoteInput: RemoteInput? = RemoteInput.Builder(KEY_TEXT_REPLY).run {
//            setLabel(replyLabel)
//            build()
//        }
//
//        // RemoteInput도 액션이기 때문에, 액션의 터치 이벤트를 처리하기 위한 pendingIntent 준비
//        val replyIntent = Intent(this,MainActivity::class.java)
//        val replyPendingIntent = PendingIntent.getBroadcast(this, 30, replyIntent, PendingIntent.FLAG_MUTABLE)
//
//        // 원격 입력 액션 등록
//        builder.addAction(
//            NotificationCompat.Action.Builder(
//                R.drawable.send,
//                "답장",
//                replyPendingIntent
//            ).addRemoteInput(remoteInput).build()
//        )
//
//        val replyTxt = RemoteInput.getResultsFromIntent(intent)
//            ?.getCharSequence("key_text_replay")
//
//        manager.notify(11, builder.build())

        /**
         * 프로그레스
         */
        builder.setProgress(100,0,false)
        manager.notify(11,builder.build())

        thread{
            for(i in 1..100){
                builder.setProgress(100,i,false)
                manager.notify(11,builder.build())
                SystemClock.sleep(100)
            }
        }
        // manager.notify(11, builder.build())

    }
}