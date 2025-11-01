package com.example.smartgym.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseTemplateResponse(
    val id: String,
    val name: String,
    @SerialName("muscle_group")
    val muscleGroup: String,
    val description: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null
)