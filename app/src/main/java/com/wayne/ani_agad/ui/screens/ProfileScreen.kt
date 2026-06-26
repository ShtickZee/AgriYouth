package com.wayne.ani_agad.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wayne.ani_agad.data.AppDatabase
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(onLogout: () -> Unit, onAdminClick: () -> Unit) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val cropDao = remember { db.cropDao() }
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    
    var userName by remember { mutableStateOf("Loading...") }
    var userEmail by remember { mutableStateOf(auth.currentUser?.email ?: "") }
    var userPhone by remember { mutableStateOf("") }
    var harvestedCount by remember { mutableIntStateOf(0) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val plantedCount by cropDao.getAllCropBatches(userId).collectAsState(initial = emptyList())

    // Hidden Admin Trigger
    var tapCount by remember { mutableStateOf(0) }
    LaunchedEffect(tapCount) {
        if (tapCount > 0) {
            delay(2000L) // Reset if no tap for 2 seconds
            tapCount = 0
        }
    }

    // Dialog States
    var showEmailDialog by remember { mutableStateOf(false) }
    var showPhoneDialog by remember { mutableStateOf(false) }
    var editValue by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        userName = snapshot.getString("fullName") ?: "Unknown User"
                        userPhone = snapshot.getString("phoneNumber") ?: ""
                        harvestedCount = snapshot.getLong("harvestedCount")?.toInt() ?: 0
                    }
                }
        }
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Profile Info Tile
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { 
                                tapCount++
                                if (tapCount >= 3) {
                                    tapCount = 0
                                    onAdminClick()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("🌱", fontSize = 40.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        Text(userName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(userEmail, style = MaterialTheme.typography.bodyMedium, color = Color.Black.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("My Stats", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatTile("Planted", plantedCount.size.toString(), Modifier.weight(1f))
                StatTile("Harvested", harvestedCount.toString(), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Settings", style = MaterialTheme.typography.titleLarge, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    SettingsItem(Icons.Default.Email, "Change Email") {
                        editValue = userEmail
                        showEmailDialog = true
                    }
                    SettingsItem(Icons.Default.Phone, "Change Phone Number") {
                        editValue = userPhone
                        showPhoneDialog = true
                    }
                    SettingsItem(Icons.Default.PhotoCamera, "Change Profile Picture") {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Logout Account", style = MaterialTheme.typography.titleMedium)
            }
        }

        // Email Dialog
        if (showEmailDialog) {
            AlertDialog(
                onDismissRequest = { showEmailDialog = false },
                title = { Text("Update Email") },
                text = {
                    OutlinedTextField(value = editValue, onValueChange = { editValue = it }, label = { Text("New Email") })
                },
                confirmButton = {
                    Button(onClick = {
                        isUpdating = true
                        auth.currentUser?.updateEmail(editValue)?.addOnCompleteListener {
                            FirebaseFirestore.getInstance().collection("users").document(userId).update("email", editValue)
                            userEmail = editValue
                            isUpdating = false
                            showEmailDialog = false
                        }
                    }, enabled = !isUpdating) { Text("Save") }
                },
                dismissButton = { TextButton(onClick = { showEmailDialog = false }) { Text("Cancel") } }
            )
        }

        // Phone Dialog
        if (showPhoneDialog) {
            AlertDialog(
                onDismissRequest = { showPhoneDialog = false },
                title = { Text("Update Phone") },
                text = {
                    OutlinedTextField(value = editValue, onValueChange = { editValue = it }, label = { Text("Phone Number") })
                },
                confirmButton = {
                    Button(onClick = {
                        isUpdating = true
                        FirebaseFirestore.getInstance().collection("users").document(userId).update("phoneNumber", editValue).addOnSuccessListener {
                            userPhone = editValue
                            isUpdating = false
                            showPhoneDialog = false
                        }
                    }, enabled = !isUpdating) { Text("Save") }
                },
                dismissButton = { TextButton(onClick = { showPhoneDialog = false }) { Text("Cancel") } }
            )
        }
    }
}

@Composable
fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, textColor: Color = Color.White, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, color = textColor)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
        }
    }
}
