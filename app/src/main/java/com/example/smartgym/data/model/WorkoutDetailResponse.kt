package com.example.smartgym.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDetailResponse(
    val id: String,
    val name: String,
    val weekday: String,
    val created_at: String,
    val exercises: List<ExerciseDetailResponse>
)

@Serializable
data class ExerciseDetailResponse(
    val id: String,
    val sets: Int,
    val reps: Int,
    val weight: Double?,
    val notes: String?,
    val template: ExerciseTemplateResponse
)