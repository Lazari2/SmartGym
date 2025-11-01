package com.example.smartgym.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.smartgym.Routes
import com.example.smartgym.ui.components.BottomNavBar
import com.example.smartgym.ui.theme.DarkRed

@Composable
fun MainScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = currentRoute, onNavigate = onNavigate) },
        floatingActionButton = floatingActionButton
    ) { innerPadding ->
        content(innerPadding)
    }
}