package com.example.smartgym.data.repository

import com.example.smartgym.data.model.*
import com.example.smartgym.data.network.IaGenerateResponse
import com.example.smartgym.domain.util.Resource

interface WorkoutRepository {
    suspend fun getWorkouts(token: String): Resource<List<WorkoutDetailResponse>>

    suspend fun createWorkout(
        token: String,
        request: CreateWorkoutRequest
    ): Resource<WorkoutDetailResponse>

    suspend fun getMuscleGroups(token: String): Resource<List<String>>

    suspend fun getExercisesByGroup(
        token: String,
        groupName: String
    ): Resource<List<ExerciseTemplateResponse>>

    suspend fun generateWorkoutFromIa(
        token: String,
        prompt: String
    ): Resource<IaGenerateResponse>
}