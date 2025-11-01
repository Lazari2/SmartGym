package com.example.smartgym.data.repository

import com.example.smartgym.data.model.ApiError
import com.example.smartgym.data.model.WorkoutSummaryResponse
import com.example.smartgym.data.network.AuthApiService
import com.example.smartgym.domain.util.Resource
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val json: Json
) : WorkoutRepository {

    override suspend fun getWorkouts(token: String): Resource<List<WorkoutSummaryResponse>> {
        return try {
            val response = api.getWorkouts("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()?.let {
                    json.decodeFromString<ApiError>(it).error
                }
                Resource.Error(errorMsg ?: "Erro ao buscar treinos.")
            }
        } catch (e: Exception) {
            Resource.Error("Falha na conexão: ${e.message}")
        }
    }
}