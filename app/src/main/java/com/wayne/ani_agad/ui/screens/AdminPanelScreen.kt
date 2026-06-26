package com.wayne.ani_agad.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wayne.ani_agad.notifications.WateringReceiver

@Composable
fun AdminPanelScreen() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🛡️ System Admin Panel",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        
        Text(
            text = "Testing Features: Force send tailored notifications in the new format.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AdminTestButton(
            label = "Force Harvest Notif",
            onClick = {
                triggerManualNotif(context, "Batch Alpha", "Batch Alpha > Harvest, It's harvest day! Did you harvest yet?", 5001, true)
            }
        )

        AdminTestButton(
            label = "Force Pest Notif",
            onClick = {
                triggerManualNotif(context, "Balcony Kangkong", "Balcony Kangkong > Pest, Weekly Pest Check! Inspect your leaves for Aphids.", 2001, false)
            }
        )

        AdminTestButton(
            label = "Force Weather Notif (Heat)",
            onClick = {
                triggerManualNotif(context, "Lettuce Tray", "Lettuce Tray > Weather, High Heat Detected! Check your reservoir roots.", 3001, false)
            }
        )

        AdminTestButton(
            label = "Force Weather Notif (Rain)",
            onClick = {
                triggerManualNotif(context, "Mustasa Pot", "Mustasa Pot > Weather, Heavy Rain! Move your soil pot indoors.", 4001, false)
            }
        )
    }
}

@Composable
fun AdminTestButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.1f),
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
    }
}

private fun triggerManualNotif(context: Context, batchName: String, message: String, id: Int, isHarvest: Boolean) {
    val intent = Intent(context, WateringReceiver::class.java).apply {
        putExtra("BATCH_NAME", batchName)
        putExtra("MESSAGE", message)
        putExtra("NOTIFICATION_ID", id)
        putExtra("IS_HARVEST", isHarvest)
    }
    context.sendBroadcast(intent)
}
