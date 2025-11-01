package com.example.smartgym.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

data class WorkoutData(
    val name: String,
    val setsInfo: String,
    val date: String,
    val isNewRecord: Boolean,
    val icon: ImageVector,
    val weekday: String
)