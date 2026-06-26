package com.wayne.ani_agad.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.wayne.ani_agad.MainActivity

class WateringReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        
        if (action == "ACTION_HARVEST_YES" && userId != null) {
            // User clicked YES - Increment Harvested Count in Firestore
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .update("harvestedCount", FieldValue.increment(1))
            
            // Clear the notification
            val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(notificationId)
            return
        }

        // Standard Notification Logic
        val batchName = intent.getStringExtra("BATCH_NAME") ?: "Your plant"
        val message = intent.getStringExtra("MESSAGE") ?: "Time for maintenance!"
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        val isHarvestDay = intent.getBooleanExtra("IS_HARVEST", false)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 
            notificationId, 
            notificationIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, "WATERING_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_menu_myplaces)
            .setContentTitle("Ani-Agad: $batchName")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (isHarvestDay) {
            // Add Interactive Buttons for Harvest
            val yesIntent = Intent(context, WateringReceiver::class.java).apply {
                this.action = "ACTION_HARVEST_YES"
                putExtra("NOTIFICATION_ID", notificationId)
            }
            val yesPendingIntent = PendingIntent.getBroadcast(
                context, 100, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            builder.addAction(0, "Yes", yesPendingIntent)
            builder.addAction(0, "No", null) // "No" just closes the notification
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}
