package com.example.smartgym.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.smartgym.Routes
import com.example.smartgym.ui.theme.DarkRed

@Composable
fun BottomNavBar(currentRoute: String, onNavigate: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.Black
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = DarkRed,
            selectedTextColor = DarkRed,
            unselectedIconColor = Color.LightGray,
            unselectedTextColor = Color.LightGray,
            indicatorColor = Color.Transparent
        )
        NavigationBarItem(
            selected = currentRoute == Routes.INITIAL,
            onClick = { onNavigate(Routes.INITIAL) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == Routes.CHAT,
            enabled = false,
            onClick = { onNavigate(Routes.CHAT) },
            icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat") },
            label = { Text("Chat") },
            colors = itemColors
        )

        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            enabled = false,
            onClick = { onNavigate(Routes.PROFILE) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            colors = itemColors
        )
    }
}
