package com.wayne.ani_agad.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wayne.ani_agad.data.AppDatabase
import com.wayne.ani_agad.data.CropBatch
import com.wayne.ani_agad.data.CropRepository
import com.wayne.ani_agad.notifications.NotificationScheduler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun CreateCropScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }
    val cropDao = remember { db.cropDao() }
    val firestore = FirebaseFirestore.getInstance()

    val crops = CropRepository.allCrops
    val pagerState = rememberPagerState(pageCount = { crops.size })

    var batchName by remember { mutableStateOf("") }
    var containerCount by remember { mutableStateOf("") }
    var growthMethod by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pagerState.currentPage) {
        growthMethod = crops[pagerState.currentPage].preferredMethod
    }

    val isRecommendedMethod = growthMethod == crops[pagerState.currentPage].preferredMethod

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Select Crop Type", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 48.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.height(130.dp)
            ) { page ->
                val crop = crops[page]
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (pagerState.currentPage == page) MaterialTheme.colorScheme.primaryContainer else Color.White.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = crop.emoji, fontSize = 32.sp)
                            Text(text = crop.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (pagerState.currentPage == page) Color.Black else Color.White)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = batchName,
                onValueChange = { if (!isLoading) batchName = it },
                label = { Text("Batch Name (e.g., Balcony Setup)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedLabelColor = MaterialTheme.colorScheme.tertiary, unfocusedLabelColor = Color.White.copy(alpha = 0.6f))
            )

            OutlinedTextField(
                value = containerCount,
                onValueChange = { if (!isLoading && it.all { char -> char.isDigit() }) containerCount = it },
                label = { Text("Number of Containers") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedLabelColor = MaterialTheme.colorScheme.tertiary, unfocusedLabelColor = Color.White.copy(alpha = 0.6f))
            )

            Column {
                if (!isRecommendedMethod) {
                    Text(
                        text = "Not recommended please use suggested method",
                        color = Color.Red,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                OutlinedTextField(
                    value = growthMethod,
                    onValueChange = { if (!isLoading) growthMethod = it },
                    label = { Text("Growth Method") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedLabelColor = if (isRecommendedMethod) MaterialTheme.colorScheme.tertiary else Color.Red, unfocusedLabelColor = Color.White.copy(alpha = 0.6f))
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "New to planting? Head over to the guide's section after you create your plan!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            if (isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Syncing to Cloud...", color = Color.White, style = MaterialTheme.typography.bodySmall)
                }
            }

            errorMessage?.let {
                Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null && batchName.isNotBlank() && containerCount.isNotBlank()) {
                        isLoading = true
                        errorMessage = null
                        val timestamp = System.currentTimeMillis()
                        
                        val planData = hashMapOf(
                            "ownerId" to userId,
                            "cropType" to crops[pagerState.currentPage].name,
                            "batchName" to batchName,
                            "containerCount" to (containerCount.toIntOrNull() ?: 0),
                            "growthMethod" to growthMethod,
                            "datePlanted" to timestamp
                        )

                        firestore.collection("crop_batches")
                            .add(planData)
                            .addOnSuccessListener {
                                scope.launch {
                                    val newBatch = CropBatch(
                                        ownerId = userId,
                                        cropType = crops[pagerState.currentPage].name,
                                        batchName = batchName,
                                        containerCount = containerCount.toIntOrNull() ?: 0,
                                        growthMethod = growthMethod,
                                        datePlanted = timestamp
                                    )
                                    val localId = cropDao.insertCropBatch(newBatch)
                                    
                                    // Parse days from the repository string (e.g., "30-40 Days" -> 30)
                                    val daysStr = crops[pagerState.currentPage].daysToHarvest.filter { it.isDigit() }
                                    val daysToHarvest = daysStr.take(2).toIntOrNull() ?: 30

                                    NotificationScheduler.scheduleWatering(context, localId.toInt(), batchName, growthMethod, daysToHarvest)

                                    isLoading = false
                                    navController.popBackStack()
                                }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = "Cloud sync failed: ${e.message}"
                            }
                    }
                },
                enabled = batchName.isNotBlank() && containerCount.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Plant Now", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
