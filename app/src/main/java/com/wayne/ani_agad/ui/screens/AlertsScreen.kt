package com.wayne.ani_agad.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.wayne.ani_agad.data.AppDatabase
import com.wayne.ani_agad.data.WeatherAlert
import com.wayne.ani_agad.data.WeatherService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val cropDao = remember { db.cropDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val cropBatches by cropDao.getAllCropBatches(userId).collectAsState(initial = emptyList())

    var hasNotificationPermission by remember {
        mutableStateOf(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) false else true)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasNotificationPermission = it }
    )

    // Current list of active weather alerts
    val activeAlerts = remember { mutableStateListOf<WeatherAlert>() }
    var isLoadingWeather by remember { mutableStateOf(true) }

    LaunchedEffect(cropBatches) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        try {
            val response = WeatherService.api.getForecast()
            val temp = response.currentWeather.temperature
            val rainProb = response.daily.rainProbability.firstOrNull() ?: 0
            val code = response.currentWeather.weatherCode
            val wind = response.currentWeather.windspeed

            val (emoji, weatherTitle) = when (code) {
                0 -> "☀️" to "Clear Sky"
                1, 2, 3 -> "☁️" to "Partly Cloudy"
                in 51..67, in 80..99 -> "🌧️" to "Rainy"
                else -> "🌬️" to "Windy"
            }

            val isHighHeat = temp > 32.0
            val isHighRain = rainProb > 70
            val isHighWind = wind > 20.0

            activeAlerts.clear()

            if (cropBatches.isEmpty()) {
                activeAlerts.add(WeatherAlert(
                    emoji = emoji,
                    title = "General Weather ($temp°C)",
                    subtitle = "No active crops. Conditions are $weatherTitle in Lucena City.",
                    isHighPriority = false
                ))
            } else {
                cropBatches.forEach { batch ->
                    val plantName = batch.batchName
                    val isHydro = batch.growthMethod.contains("Hydro", ignoreCase = true)
                    
                    val action = when {
                        isHighHeat && isHydro && batch.cropType.contains("Lettuce", ignoreCase = true) -> 
                            "$plantName > Weather, High Heat! Check reservoir—roots will rot if water > 30°C."
                        isHighHeat && isHydro -> 
                            "$plantName > Weather, Hot Day! Monitor water levels in your hydroponic setup."
                        isHighHeat && !isHydro -> 
                            "$plantName > Weather, Heat Alert! Perform the Knuckle Test to prevent soil drying."
                        isHighRain && !isHydro -> 
                            "$plantName > Weather, Heavy Rain! Move your soil pot indoors to prevent root rot."
                        isHighWind && batch.growthMethod.contains("Hanging", ignoreCase = true) -> 
                            "$plantName > Weather, High Winds! Secure your hanging setup to prevent falling."
                        else -> "$plantName > Weather, $weatherTitle. Conditions are stable."
                    }

                    activeAlerts.add(WeatherAlert(
                        emoji = emoji,
                        title = "$plantName ($temp°C)",
                        subtitle = action,
                        isHighPriority = isHighHeat || isHighRain || isHighWind
                    ))
                }
            }
        } catch (e: Exception) {
            if (activeAlerts.isEmpty()) {
                activeAlerts.add(WeatherAlert("⚠️", "Weather Offline", "Unable to fetch data for Lucena.", false))
            }
        } finally {
            isLoadingWeather = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tailored Weather Alerts",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        if (isLoadingWeather) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        } else {
            if (activeAlerts.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🧹", fontSize = 60.sp)
                    Text("All caught up!", color = Color.White.copy(alpha = 0.5f))
                }
            } else {
                activeAlerts.forEach { alert ->
                    key(alert.title) {
                        SwipeToDismissAlert(
                            alert = alert,
                            onDismiss = { activeAlerts.remove(alert) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Scheduled Maintenance",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📭", fontSize = 60.sp)
            Text(
                "Maintenance reminders are sent to your system notification bar.",
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissAlert(alert: WeatherAlert, onDismiss: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDismiss()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, MaterialTheme.shapes.extraLarge)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Dismiss",
                    tint = Color.White
                )
            }
        },
        content = {
            WeatherStatusCard(alert)
        }
    )
}

@Composable
fun WeatherStatusCard(alert: WeatherAlert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (alert.isHighPriority) Color.Red.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer
        ),
        border = if (alert.isHighPriority) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(Color.Red)) else null
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(alert.emoji, fontSize = 40.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (alert.isHighPriority) Color.White else Color.Black
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = alert.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = if (alert.isHighPriority) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )
        }
    }
}
