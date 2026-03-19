package org.example.walkietalkie.signaling


import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import kotlinx.coroutines.*
import org.example.walkietalkie.R
import org.example.walkietalkie.model.SignalType

class SignalListenerService : Service() {

    private val signalingService = SignalingService()

    private val serviceScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob()
    )

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        val room = intent?.getStringExtra("room") ?: return Service.START_STICKY

        val deviceId =
            intent.getStringExtra("deviceId") ?: ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                1,
                NotificationHelper.createServiceNotification(this),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            )
        } else {
            startForeground(
                1,
                NotificationHelper.createServiceNotification(this)
            )
        }

        serviceScope.launch {

            signalingService.listenForSignals { signal ->

                if(signal.room != room) return@listenForSignals

                if(signal.sender == deviceId) return@listenForSignals

                if(signal.type == SignalType.PING){

                    playPing()

                    NotificationHelper.showPingNotification(
                        this@SignalListenerService
                    )

                }

            }

        }

        return START_STICKY
    }

    private fun playPing(){

        val mp = MediaPlayer.create(
            this,
            R.raw.pingtone
        )

        mp.setOnCompletionListener {

            it.release()

        }

        mp.start()

    }

    override fun onDestroy() {

        serviceScope.cancel()

        super.onDestroy()

    }

    override fun onTaskRemoved(rootIntent: Intent?) {

        val restartIntent = Intent(
            applicationContext,
            SignalListenerService::class.java
        )

        restartIntent.putExtra(
            "room",
            rootIntent?.getStringExtra("room")
        )

        restartIntent.putExtra(
            "deviceId",
            rootIntent?.getStringExtra("deviceId")
        )

        val pendingIntent =
            android.app.PendingIntent.getService(

                this,

                1,

                restartIntent,

                android.app.PendingIntent.FLAG_IMMUTABLE

            )

        val alarmService =
            getSystemService(Context.ALARM_SERVICE)
                    as android.app.AlarmManager

        alarmService.set(

            android.app.AlarmManager.ELAPSED_REALTIME,

            SystemClock.elapsedRealtime() + 1000,

            pendingIntent

        )

        super.onTaskRemoved(rootIntent)

    }

    override fun onBind(intent: Intent?): IBinder? = null

}