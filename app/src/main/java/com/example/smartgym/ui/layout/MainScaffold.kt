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
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = currentRoute, onNavigate = onNavigate) },
        floatingActionButton = {
            if (currentRoute == Routes.INITIAL) {
                FloatingActionButton(
                    onClick = { onNavigate(Routes.ADD_WORKOUT) },
                    containerColor = DarkRed
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Treino", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}