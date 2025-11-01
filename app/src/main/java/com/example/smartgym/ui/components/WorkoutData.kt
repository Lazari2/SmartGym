package com.example.smartgym.ui.components

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.smartgym.data.model.ExerciseDetailResponse

data class WorkoutData(
    val name: String,
    val date: String,
    val isNewRecord: Boolean,
    val icon: ImageVector,
    val weekday: String,
    val exercises: List<ExerciseDetailResponse>
)