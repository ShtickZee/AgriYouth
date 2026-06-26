package com.wayne.ani_agad.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object NotificationScheduler {
    fun scheduleWatering(context: Context, batchId: Int, batchName: String, method: String, daysToHarvest: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 1. Schedule Watering/Check-in Alarms
        if (method.contains("Hydroponics", ignoreCase = true)) {
            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 7)
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
            }
            scheduleAlarm(context, alarmManager, batchId, batchName, "$batchName > Weather, Hey! It's been 7 days. Check on your hydroponics cause the water level might be low.", calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7)
        } else {
            val morning = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8)
                set(Calendar.MINUTE, 0)
                if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
            }
            scheduleAlarm(context, alarmManager, batchId * 10, batchName, "$batchName > Weather, Time for the Knuckle Test! Morning watering session.", morning.timeInMillis, AlarmManager.INTERVAL_DAY)

            val evening = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 17)
                set(Calendar.MINUTE, 0)
                if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
            }
            scheduleAlarm(context, alarmManager, batchId * 10 + 1, batchName, "$batchName > Weather, Time for the Knuckle Test! Evening watering session.", evening.timeInMillis, AlarmManager.INTERVAL_DAY)
        }

        // 2. Schedule PEST ALERT (Every 7 Days)
        val pestCalendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 7)
            set(Calendar.HOUR_OF_DAY, 14) // 2 PM
            set(Calendar.MINUTE, 30)
        }
        scheduleAlarm(context, alarmManager, batchId + 2000, batchName, "$batchName > Pest, Weekly Pest Check! Inspect your leaves for Aphids or Flea Beetles as per your guide.", pestCalendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7)

        // 3. Schedule Harvest Day Notification (Based on Crop Days)
        val harvestCalendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, daysToHarvest)
            set(Calendar.HOUR_OF_DAY, 10) // 10 AM on Harvest Day
            set(Calendar.MINUTE, 0)
        }
        
        val harvestIntent = Intent(context, WateringReceiver::class.java).apply {
            putExtra("BATCH_NAME", batchName)
            putExtra("MESSAGE", "$batchName > Harvest, It's harvest day! Did you harvest yet?")
            putExtra("NOTIFICATION_ID", batchId + 5000)
            putExtra("IS_HARVEST", true)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, batchId + 5000, harvestIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, harvestCalendar.timeInMillis, pendingIntent)
    }

    private fun scheduleAlarm(context: Context, alarmManager: AlarmManager, id: Int, batchName: String, message: String, time: Long, interval: Long) {
        val intent = Intent(context, WateringReceiver::class.java).apply {
            putExtra("BATCH_NAME", batchName)
            putExtra("MESSAGE", message)
            putExtra("NOTIFICATION_ID", id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, interval, pendingIntent)
    }

    fun cancelWatering(context: Context, batchId: Int, method: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WateringReceiver::class.java)

        if (method.contains("Hydroponics", ignoreCase = true)) {
            alarmManager.cancel(PendingIntent.getBroadcast(context, batchId, intent, PendingIntent.FLAG_IMMUTABLE))
        } else {
            alarmManager.cancel(PendingIntent.getBroadcast(context, batchId * 10, intent, PendingIntent.FLAG_IMMUTABLE))
            alarmManager.cancel(PendingIntent.getBroadcast(context, batchId * 10 + 1, intent, PendingIntent.FLAG_IMMUTABLE))
        }
        // Cancel Pest Alert
        alarmManager.cancel(PendingIntent.getBroadcast(context, batchId + 2000, intent, PendingIntent.FLAG_IMMUTABLE))
        // Cancel Harvest notif
        alarmManager.cancel(PendingIntent.getBroadcast(context, batchId + 5000, intent, PendingIntent.FLAG_IMMUTABLE))
    }
}
