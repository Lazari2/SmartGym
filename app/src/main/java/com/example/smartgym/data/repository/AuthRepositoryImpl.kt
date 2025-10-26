package com.example.smartgym.data.repository

import com.example.smartgym.data.model.ApiError
import com.example.smartgym.data.model.AuthResponse
import com.example.smartgym.data.model.RegisterResponse
import com.example.smartgym.data.network.AuthApiService
import com.example.smartgym.data.network.LoginRequest
import com.example.smartgym.data.network.RegisterRequest
import kotlinx.serialization.json.Json
import com.example.smartgym.domain.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val json: Json
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): Resource<AuthResponse> {
        return try {
            val response = api.login(loginRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                Resource.Error(errorMsg ?: "Email ou senha inválidos.")
            }
        } catch (e: Exception) {
            Resource.Error("Falha na conexão: ${e.message}")
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): Resource<RegisterResponse> {
        return try {
            val response = api.register(registerRequest)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = parseError(response.errorBody()?.string())
                Resource.Error(errorMsg ?: "Não foi possível criar a conta.")
            }
        } catch (e: Exception) {
            Resource.Error("Falha na conexão: ${e.message}")
        }
    }
    private fun parseError(errorBody: String?): String? {
        return try {
            errorBody?.let {
                json.decodeFromString<ApiError>(it).error
            }
        } catch (e: Exception) {
            null
        }
    }
}