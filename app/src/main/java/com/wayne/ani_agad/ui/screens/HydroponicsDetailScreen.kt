package com.wayne.ani_agad.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wayne.ani_agad.data.GrowthMethodRepository

@Composable
fun HydroponicsDetailScreen() {
    var selectedLayoutIndex by remember { mutableStateOf(0) }
    val currentLayout = GrowthMethodRepository.hydroLayouts[selectedLayoutIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Main Title
        Text(
            text = "PASSIVE HYDROPONICS",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        Text(
            text = "The 'No-Pump' Water Method",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.7f)
        )

        // Introductory Essay Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = GrowthMethodRepository.hydroIntro,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )
            }
        }

        // Choose Your Layout Section
        Text(
            text = "Choose Your Layout",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Tab-style Switcher
        ScrollableTabRow(
            selectedTabIndex = selectedLayoutIndex,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            edgePadding = 0.dp,
            divider = {}
        ) {
            GrowthMethodRepository.hydroLayouts.forEachIndexed { index, layout ->
                Tab(
                    selected = selectedLayoutIndex == index,
                    onClick = { selectedLayoutIndex = index }
                ) {
                    Text(
                        text = layout.id.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selectedLayoutIndex == index) MaterialTheme.colorScheme.tertiary else Color.White.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // 1. Description Tile
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = currentLayout.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = currentLayout.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }
        }

        // 2. Full Instructional Essay Tile
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Instructional Essay",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = currentLayout.instructions,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 22.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
