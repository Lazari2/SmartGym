package com.example.smartgym.data.network

import com.example.smartgym.data.model.AuthResponse
import com.example.smartgym.data.model.CreateWorkoutRequest
import com.example.smartgym.data.model.RegisterResponse
import com.example.smartgym.data.model.WorkoutDetailResponse
import com.example.smartgym.data.model.ExerciseTemplateResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String
)

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("api/workouts")
    suspend fun getWorkouts(
        @Header("Authorization") token: String
    ): Response<List<WorkoutDetailResponse>>

    @POST("api/workouts")
    suspend fun createWorkout(
        @Header("Authorization") token: String,
        @Body workoutRequest: CreateWorkoutRequest
    ): Response<WorkoutDetailResponse>

    @GET("api/exercises/groups")
    suspend fun getMuscleGroups(
        @Header("Authorization") token: String
    ): Response<List<String>>

    @GET("api/exercises/group/{group_name}")
    suspend fun getExercisesByGroup(
        @Header("Authorization") token: String,
        @Path("group_name") groupName: String
    ): Response<List<ExerciseTemplateResponse>>
}