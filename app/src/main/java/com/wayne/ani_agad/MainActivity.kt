package com.wayne.ani_agad

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wayne.ani_agad.navigation.AppNavigation
import com.wayne.ani_agad.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        createNotificationChannel()
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Watering Reminders"
            val descriptionText = "Notifications for crop maintenance and watering"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("WATERING_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
