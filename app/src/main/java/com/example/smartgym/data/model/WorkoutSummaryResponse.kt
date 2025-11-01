package com.example.smartgym.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutSummaryResponse(
    val id: String,
    val name: String,
    val date: String,
    @SerialName("total_sets")
    val totalSets: Int,
    val weekday: String
)