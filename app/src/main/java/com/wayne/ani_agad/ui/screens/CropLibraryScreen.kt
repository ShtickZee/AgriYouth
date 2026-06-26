package com.wayne.ani_agad.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wayne.ani_agad.data.CropRepository
import com.wayne.ani_agad.navigation.Screen

@Composable
fun CropLibraryScreen(navController: NavController) {
    val crops = CropRepository.allCrops

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Large Tile for Featured/Intro
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.CenterStart) {
                Column {
                    Text("Crop Index", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Select a vegetable to learn how to grow it.", style = MaterialTheme.typography.bodyMedium, color = Color.Black.copy(alpha = 0.6f))
                }
            }
        }

        // Grid formation for all vegetables
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(crops) { crop ->
                CropGridTile(
                    name = crop.name,
                    emoji = crop.emoji,
                    onClick = { navController.navigate("crop_detail/${crop.id}") }
                )
            }
        }
    }
}

@Composable
fun CropGridTile(name: String, emoji: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
