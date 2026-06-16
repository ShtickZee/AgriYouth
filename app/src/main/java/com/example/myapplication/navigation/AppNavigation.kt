package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.AlertsScreen
import com.example.myapplication.ui.screens.CreateCropScreen
import com.example.myapplication.ui.screens.CropsScreen
import com.example.myapplication.ui.screens.ProfileScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Alerts : Screen("alerts", "Alerts", Icons.Default.Notifications)
    object Crops : Screen("crops", "Crops", Icons.Default.Spa)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object CreateCrop : Screen("create_crop", "New Crop", Icons.Default.Spa)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Alerts,
        Screen.Crops,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Crops.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Alerts.route) { AlertsScreen() }
            composable(Screen.Crops.route) { CropsScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.CreateCrop.route) { CreateCropScreen(navController) }
        }
    }
}
