package com.example.smartgym.data.model

import kotlinx.serialization.Serializable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Serializable
data class CreateWorkoutRequest(
    val name: String,
    val weekday: String,
    val exercises: List<ExerciseRequest>
)
@Serializable
@Parcelize
data class ExerciseRequest(
    @SerialName("template_id")
    val templateId: String,

    @SerialName("sets")
    val sets: Int,

    @SerialName("reps")
    val reps: Int,

    @SerialName("weight")
    val weight: Double? = null,

    @SerialName("notes")
    val notes: String? = null
) : Parcelable