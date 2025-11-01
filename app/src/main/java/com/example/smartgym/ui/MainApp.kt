package com.example.smartgym.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.smartgym.Routes
import com.example.smartgym.ui.layout.MainScaffold
import com.example.smartgym.ui.screens.ChatScreen
import com.example.smartgym.ui.screens.InitialScreen
import com.example.smartgym.ui.screens.ProfileScreen
import com.example.smartgym.ui.screens.AddWorkoutScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val onNavigate: (String) -> Unit = { route ->
        navController.navigate(route) {
            if (currentRoute == Routes.ADD_WORKOUT && route != Routes.ADD_WORKOUT) {
                // When navigating away from AddWorkoutScreen, pop it from the back stack
                popUpTo(Routes.INITIAL) { inclusive = false }
            } else {
                // Standard navigation for bottom bar items
                launchSingleTop = true
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                restoreState = true
            }
        }
    }

    MainScaffold(
        currentRoute = currentRoute ?: Routes.INITIAL,
        onNavigate = onNavigate
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.INITIAL,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.INITIAL) { InitialScreen() }
            composable(Routes.CHAT) { ChatScreen() }
            composable(Routes.PROFILE) { ProfileScreen() }
            composable(Routes.ADD_WORKOUT) { AddWorkoutScreen(onNavigateBack = { navController.popBackStack() }) }
        }
    }
}