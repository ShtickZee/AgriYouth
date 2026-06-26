package com.wayne.ani_agad.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wayne.ani_agad.navigation.Screen

@Composable
fun GuidesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Section 1: Crop Library
        GuideCategoryTile(
            title = "Crop Library",
            subtitle = "Detailed guides for various vegetables",
            emoji = "🥬",
            containerColor = MaterialTheme.colorScheme.primaryContainer, // Mint
            onClick = { navController.navigate(Screen.CropLibrary.route) }
        )

        // Section 2: Growth Methods
        GuideCategoryTile(
            title = "Growth Methods",
            subtitle = "Learn different ways to plant",
            emoji = "🏗️",
            containerColor = MaterialTheme.colorScheme.secondary, // Sage
            onClick = { navController.navigate("growth_methods_list") }
        )
    }
}

@Composable
fun GuideCategoryTile(
    title: String,
    subtitle: String,
    emoji: String,
    containerColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 48.sp)
            }
        }
    }
}
