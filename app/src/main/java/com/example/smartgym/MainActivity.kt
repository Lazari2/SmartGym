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
import androidx.navigation.navigation
import com.example.smartgym.ui.AppNavigation
import com.example.smartgym.ui.layout.MainScaffold
import com.example.smartgym.ui.screens.ChatScreen
import com.example.smartgym.ui.screens.InitialScreen
import com.example.smartgym.ui.screens.LoginScreen
import com.example.smartgym.ui.screens.ProfileScreen
import com.example.smartgym.ui.theme.SmartGymTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartGymTheme {
                AppNavigation()
            }
        }
    }
}
