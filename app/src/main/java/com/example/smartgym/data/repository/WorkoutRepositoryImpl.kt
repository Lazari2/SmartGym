package com.example.smartgym.data.repository

import android.util.Log
import com.example.smartgym.data.model.*
import com.example.smartgym.data.network.AuthApiService
import com.example.smartgym.domain.util.Resource
import kotlinx.serialization.json.Json
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val json: Json
) : WorkoutRepository {

    override suspend fun getWorkouts(token: String): Resource<List<WorkoutDetailResponse>> {
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

    override suspend fun createWorkout(
        token: String,
        request: CreateWorkoutRequest
    ): Resource<WorkoutDetailResponse> {
        return try {
            val response = api.createWorkout("Bearer $token", request)
            Log.d("RepoCreateWorkout", "Resposta da API recebida.")
            Log.d("RepoCreateWorkout", "Foi Sucesso (status 2xx)? ${response.isSuccessful}")
            Log.d("RepoCreateWorkout", "Corpo (body) parseado pelo Retrofit: ${response.body()}")
            if (response.isSuccessful && response.body() != null) {
                Log.d("RepoCreateWorkout", "SUCESSO no parsing! Retornando Resource.Success.")
                Resource.Success(response.body()!!)
            } else {
                Log.e("RepoCreateWorkout", "ERRO no parsing! 'response.body()' está nulo ou a chamada falhou.")
                val errorMsg = response.errorBody()?.string()?.let { json.decodeFromString<ApiError>(it).error }
                Resource.Error(errorMsg ?: "Erro ao salvar treino.")
            }
        } catch (e: Exception) {
            Log.e("RepoCreateWorkout", "ERRO de rede/exceção: ${e.message}")
            Resource.Error("Falha na conexão: ${e.message}")
        }
    }

    override suspend fun getMuscleGroups(token: String): Resource<List<String>> {
        return try {
            val response = api.getMuscleGroups("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()?.let { json.decodeFromString<ApiError>(it).error }
                Resource.Error(errorMsg ?: "Erro ao buscar grupos musculares.")
            }
        } catch (e: Exception) {
            Resource.Error("Falha na conexão: ${e.message}")
        }
    }

    override suspend fun getExercisesByGroup(
        token: String,
        groupName: String
    ): Resource<List<ExerciseTemplateResponse>> {
        return try {
            val response = api.getExercisesByGroup("Bearer $token", groupName)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()?.let { json.decodeFromString<ApiError>(it).error }
                Resource.Error(errorMsg ?: "Erro ao buscar exercícios.")
            }
        } catch (e: Exception) {
            Resource.Error("Falha na conexão: ${e.message}")
        }
    }

}