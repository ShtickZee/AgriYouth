package com.wayne.ani_agad.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wayne.ani_agad.data.AppDatabase
import com.wayne.ani_agad.data.CropBatch
import com.wayne.ani_agad.navigation.Screen
import com.wayne.ani_agad.notifications.NotificationScheduler
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CropsScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }
    val cropDao = remember { db.cropDao() }
    
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val cropBatches by cropDao.getAllCropBatches(userId).collectAsState(initial = emptyList())

    var isMenuExpanded by remember { mutableStateOf(false) }
    var isDeleteMode by remember { mutableStateOf(false) }
    var batchToDelete by remember { mutableStateOf<CropBatch?>(null) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            CropsSpeedDial(
                isExpanded = isMenuExpanded,
                onToggle = { isMenuExpanded = !isMenuExpanded },
                onAddClick = {
                    isMenuExpanded = false
                    navController.navigate(Screen.CreateCrop.route)
                },
                onDeleteModeToggle = {
                    isMenuExpanded = false
                    isDeleteMode = !isDeleteMode
                },
                isDeleteModeActive = isDeleteMode
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                if (cropBatches.isEmpty()) {
                    EmptyCropsState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 24.dp, bottom = 80.dp)
                    ) {
                        items(cropBatches) { batch ->
                            CropPlanTile(
                                batch = batch,
                                isDeleteMode = isDeleteMode,
                                onDeleteClick = { batchToDelete = batch }
                            )
                        }
                    }
                }
            }

            if (batchToDelete != null) {
                AlertDialog(
                    onDismissRequest = { batchToDelete = null },
                    title = { Text("Delete Plan") },
                    text = { Text("Are you sure you want to permanently delete '${batchToDelete?.batchName}'?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                batchToDelete?.let { batch ->
                                    scope.launch {
                                        // Cancel scheduled notifications
                                        NotificationScheduler.cancelWatering(context, batch.id, batch.growthMethod)
                                        
                                        // Delete from database
                                        cropDao.deleteCropBatch(batch)
                                        batchToDelete = null
                                    }
                                }
                            }
                        ) {
                            Text("Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { batchToDelete = null }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CropsSpeedDial(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteModeToggle: () -> Unit,
    isDeleteModeActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
        ) {
            Surface(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                modifier = Modifier.width(56.dp).shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(onClick = onAddClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add Crop", tint = Color.Black)
                    }
                    IconButton(onClick = onDeleteModeToggle) {
                        Icon(
                            if (isDeleteModeActive) Icons.Default.Close else Icons.Default.Remove,
                            contentDescription = "Toggle Delete Mode",
                            tint = if (isDeleteModeActive) Color.Red else Color.Black
                        )
                    }
                    HorizontalDivider(modifier = Modifier.width(32.dp), color = Color.Black.copy(alpha = 0.1f))
                }
            }
        }

        Surface(
            onClick = onToggle,
            color = MaterialTheme.colorScheme.tertiary,
            shape = if (isExpanded) RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp) else RoundedCornerShape(16.dp),
            modifier = Modifier.size(56.dp).shadow(if (isExpanded) 8.dp else 4.dp, if (isExpanded) RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp) else RoundedCornerShape(16.dp))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    if (isExpanded) Icons.Default.Close else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Toggle Menu",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun CropPlanTile(
    batch: CropBatch,
    isDeleteMode: Boolean,
    onDeleteClick: () -> Unit
) {
    val sdf = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val dateStr = sdf.format(Date(batch.datePlanted))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isDeleteMode) 8.dp else 0.dp, MaterialTheme.shapes.extraLarge),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (isDeleteMode) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when(batch.cropType) {
                            "PECHAY (Pak Choi)" -> "🥬"
                            "KANGKONG (Water Spinach)" -> "🌿"
                            "MUSTASA (Indian Mustard / Leaf Mustard)" -> "🌱"
                            "LETTUCE (Heat-Tolerant Varieties)" -> "🥗"
                            "SIBUYAS DAHON (Spring Onion)" -> "🧅"
                            else -> "🌶️"
                        },
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = batch.batchName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${batch.cropType} • ${batch.growthMethod}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Text(
                    text = "Planted: $dateStr • ${batch.containerCount} Containers",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }

            if (isDeleteMode) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp).background(Color.Red, CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.White)
                }
            } else {
                Text(
                    text = "View",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyCropsState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🌱", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Don't have any crops yet?", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Text(text = "Let's get to planting!", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.7f))
    }
}
