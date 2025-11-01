package com.example.smartgym.data.repository

import com.example.smartgym.data.model.WorkoutSummaryResponse
import com.example.smartgym.domain.util.Resource

interface WorkoutRepository {
    suspend fun getWorkouts(token: String): Resource<List<WorkoutSummaryResponse>>
}