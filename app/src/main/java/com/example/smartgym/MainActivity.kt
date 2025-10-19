package com.example.smartgym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartgym.ui.layout.MainScaffold
import com.example.smartgym.ui.screens.ChatScreen
import com.example.smartgym.ui.screens.InitialScreen
import com.example.smartgym.ui.screens.ProfileScreen
import com.example.smartgym.ui.theme.SmartGymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGymTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    MainScaffold(
        currentRoute = currentRoute ?: Routes.INITIAL,
        onNavigate = { route ->
            navController.navigate(route) {
                // evita empilhar telas repetidas na navegação
                launchSingleTop = true
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                restoreState = true
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.INITIAL,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.INITIAL) { InitialScreen() }
            composable(Routes.CHAT) { ChatScreen() }
            composable(Routes.PROFILE) { ProfileScreen() }
        }
    }
}