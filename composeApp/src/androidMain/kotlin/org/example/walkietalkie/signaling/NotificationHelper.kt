package org.example.walkietalkie.signaling

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import org.example.walkietalkie.R

object NotificationHelper {

    private const val CHANNEL="walkie_channel"

    fun createServiceNotification(
        context: Context
    ): Notification {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val manager =
                context.getSystemService(
                    NotificationManager::class.java
                )

            val channel = NotificationChannel(

                CHANNEL,

                "Walkie Talkie",

                NotificationManager.IMPORTANCE_HIGH

            )

            manager.createNotificationChannel(channel)

        }

        return NotificationCompat.Builder(
            context,
            CHANNEL
        )

            .setContentTitle("Walkie Talkie Active")

            .setContentText("Listening for signals")

            .setSmallIcon(R.mipmap.ic_launcher)

            .setPriority(NotificationCompat.PRIORITY_HIGH)

            .build()

    }

    fun showPingNotification(
        context: Context
    ){

        val manager =
            context.getSystemService(
                NotificationManager::class.java
            )

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL
            )

                .setContentTitle("Incoming Talk Request")

                .setContentText("Someone wants to talk")

                .setPriority(
                    NotificationCompat.PRIORITY_MAX
                )

                .setSmallIcon(
                    R.mipmap.ic_launcher
                )

                .build()

        manager.notify(2,notification)

    }

}