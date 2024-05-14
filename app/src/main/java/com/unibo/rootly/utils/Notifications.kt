package com.unibo.rootly.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.unibo.rootly.MainActivity
import com.unibo.rootly.R

object Notifications {
    private lateinit var applicationContext: Context

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    fun sendNotification(title: String, text: String? = null) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = applicationContext.packageName

        val channel = NotificationChannel(
            channelId,
            "Channel Name",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.logo_round)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(0, notificationBuilder.build())
    }
}