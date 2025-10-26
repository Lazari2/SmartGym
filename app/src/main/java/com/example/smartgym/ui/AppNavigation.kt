package com.example.smartgym.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.smartgym.MainApp
import com.example.smartgym.Routes
import com.example.smartgym.ui.screens.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GRAPH
    ) {

        navigation(
            startDestination = Routes.LOGIN,
            route = Routes.AUTH_GRAPH
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Routes.REGISTER)
                    }
                )
            }

            composable(Routes.REGISTER) {
                // RegisterScreen( ... )
            }
        }

        composable(Routes.MAIN_GRAPH) {
            MainApp()
        }
    }
}