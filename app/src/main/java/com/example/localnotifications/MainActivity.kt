package com.example.localnotifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private var notificationManager: NotificationManager? = null
    private var channelID = "com.example.Mad-255-Local_Notifications"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1: Button = findViewById<Button>(R.id.btn)
        btn1.setOnClickListener {
            // send notify
            sendNotification("Example Notification", "This is an example.")
        }

        // create default channel
        createNotificationChannel(
            channelID,
            "Local Notify Default"
        )

        //create notify service
        notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun sendNotification(title: String, content: String) {
        val notificationId = 101
        val icon: Icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
        val resultIntent = Intent(this, ResultActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0)
//        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, FLAG_IMMUTABLE)
        val action: Notification.Action = Notification.Action.Builder(icon, "Open", pendingIntent).build()
        val notification = Notification.Builder(this@MainActivity, channelID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelID)
            .setColor(Color.GREEN)
            .setContentIntent(pendingIntent)
            .setActions(action)
            .setNumber(notificationId)
            .build()

        notificationManager?.notify(notificationId, notification)
    }

    fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300)
        }

        notificationManager?.createNotificationChannel(channel)
    }
}