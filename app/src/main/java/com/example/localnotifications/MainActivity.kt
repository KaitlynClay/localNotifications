package com.example.localnotifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager
    var channelID = "com.example.localnotifications"
    val POST_IT = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //create notify service
        notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager

        // create default channel
        createNotificationChannel(
            channelID,
            "Local Notify Default"
        )

        val btn1: Button = findViewById<Button>(R.id.btn)

        btn1.setOnClickListener {
            // Check if the permission is granted
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted, proceed to send the notification
                sendNotification("Example Notification", "This is an example.")
            } else {
                // Permission is not granted, request for it
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), POST_IT)
            }
        }

    }

    private fun checkForPermission(permission: String, name: String, requestCode: Int, function: () -> () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    ContextCompat.checkSelfPermission(applicationContext,permission) == PackageManager.PERMISSION_GRANTED -> {
                        Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                    }
                    shouldShowRequestPermissionRationale(permission) -> {
                        showAskMsg(permission, name, requestCode)
                    }
                    else -> {
                        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun weOK(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name is refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name is granted", Toast.LENGTH_SHORT).show()
            }
        }
        when (requestCode) {
            POST_IT -> weOK("Post")
        }
    }



    fun sendNotification(title: String, content: String) {
        val notificationId = 101
        val icon: Icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
        val resultIntent = Intent(this, ResultActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)
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
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300)
        }

        notificationManager?.createNotificationChannel(channel)
    }

    private fun showAskMsg(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)

        builder.apply {
            setMessage("I need permission for $name to be used in this app")
            setTitle("I need permission")
            setPositiveButton("OK") { dialog, which ->
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            }
        }
    }
}



