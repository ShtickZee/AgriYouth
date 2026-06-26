package com.wayne.ani_agad.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wayne.ani_agad.ui.screens.*
import com.wayne.ani_agad.ui.theme.AppGradient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Person)
    object Register : Screen("register", "Create Account", Icons.Default.Person)
    object Alerts : Screen("alerts", "Alerts", Icons.Default.Notifications)
    object Crops : Screen("crops", "My Crops", Icons.Default.Spa)
    object Guides : Screen("guides", "Guides", Icons.Default.Spa)
    object CropLibrary : Screen("crop_library", "Crop Index", Icons.Default.Spa)
    object Profile : Screen("profile", "My Profile", Icons.Default.Person)
    object CreateCrop : Screen("create_crop", "New Crop", Icons.Default.Spa)
    object AdminPanel : Screen("admin_panel", "Admin Panel", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    
    // Listen to Firebase Auth state
    var currentUser by remember { mutableStateOf(auth.currentUser) }
    
    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { currentUser = it.currentUser }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    val isAuthenticated = currentUser != null
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Map routes to human-readable titles
    val currentTitle = when {
        currentRoute == Screen.Register.route -> Screen.Register.title
        currentRoute == Screen.Alerts.route -> Screen.Alerts.title
        currentRoute == Screen.Crops.route -> Screen.Crops.title
        currentRoute == Screen.Guides.route -> Screen.Guides.title
        currentRoute == Screen.CropLibrary.route -> Screen.CropLibrary.title
        currentRoute?.startsWith("crop_detail") == true -> "Crop Guide"
        currentRoute == Screen.Profile.route -> Screen.Profile.title
        currentRoute == Screen.CreateCrop.route -> Screen.CreateCrop.title
        currentRoute == Screen.AdminPanel.route -> Screen.AdminPanel.title
        else -> "Ani-Agad"
    }

    val showBackButton = currentRoute == Screen.Profile.route || 
                        currentRoute == Screen.CreateCrop.route ||
                        currentRoute == Screen.Register.route ||
                        currentRoute == Screen.CropLibrary.route ||
                        currentRoute?.startsWith("crop_detail") == true ||
                        currentRoute == Screen.AdminPanel.route

    val sidebarItems = listOf(Screen.Crops, Screen.Alerts, Screen.Guides)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isAuthenticated && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route && !showBackButton,
        drawerContent = {
            if (isAuthenticated) {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                    modifier = Modifier.width(300.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 32.dp)) {
                            Text("🌱", fontSize = 40.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Ani-Agad", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        
                        HorizontalDivider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(bottom = 24.dp))
                        
                        sidebarItems.forEach { screen ->
                            NavigationDrawerItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                label = { Text(screen.title, fontWeight = FontWeight.SemiBold) },
                                selected = currentRoute == screen.route,
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedIconColor = Color.Black,
                                    selectedTextColor = Color.Black,
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f)
                                ),
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    if (currentRoute != screen.route) {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                modifier = Modifier.padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                if (isAuthenticated && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.4f))
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                            if (showBackButton) {
                                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                                }
                            } else {
                                IconButton(onClick = { scope.launch { drawerState.open() } }, modifier = Modifier.align(Alignment.CenterStart)) {
                                    Icon(Icons.Default.Menu, contentDescription = "Open Menu", tint = Color.White)
                                }
                            }

                            Text(currentTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)

                            if (currentRoute != Screen.Profile.route) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer).clickable { navController.navigate(Screen.Profile.route) }.align(Alignment.CenterEnd),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🌱", fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().background(AppGradient).padding(if (isAuthenticated && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) innerPadding else PaddingValues(0.dp))) {
                NavHost(
                    navController = navController,
                    startDestination = if (isAuthenticated) Screen.Crops.route else Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginSuccess = { 
                                // State listener will handle the actual navigation
                            },
                            onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                        )
                    }
                    composable(Screen.Register.route) {
                        RegisterScreen(onRegisterSuccess = { 
                                // State listener will handle the actual navigation
                        })
                    }
                    composable(Screen.Alerts.route) { AlertsScreen() }
                    composable(Screen.Guides.route) { GuidesScreen(navController) }
                    composable("growth_methods_list") { GrowthMethodsListScreen(navController) }
                    composable(Screen.CropLibrary.route) { CropLibraryScreen(navController) }
                    composable("hydro_detail") { HydroponicsDetailScreen() }
                    composable("method_detail/{methodId}") { backStackEntry ->
                        MethodDetailScreen(backStackEntry.arguments?.getString("methodId"))
                    }
                    composable("crop_detail/{cropId}") { backStackEntry ->
                        CropDetailScreen(backStackEntry.arguments?.getString("cropId"))
                    }
                    composable(Screen.Crops.route) { CropsScreen(navController) }
                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            onLogout = {
                                auth.signOut()
                            },
                            onAdminClick = {
                                navController.navigate(Screen.AdminPanel.route)
                            }
                        )
                    }
                    composable(Screen.AdminPanel.route) { AdminPanelScreen() }
                    composable(Screen.CreateCrop.route) { CreateCropScreen(navController) }
                }
            }
        }
    }
}
