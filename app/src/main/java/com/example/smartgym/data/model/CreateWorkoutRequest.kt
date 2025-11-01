package com.example.smartgym.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateWorkoutRequest(
    val name: String,
    val weekday: String,
    val exercises: List<ExerciseRequest>
)

@Serializable
data class ExerciseRequest(
    @SerialName("template_id")
    val templateId: String,
    val sets: Int,
    val reps: String,
    val weight: Double? = null,
    val notes: String? = null
)