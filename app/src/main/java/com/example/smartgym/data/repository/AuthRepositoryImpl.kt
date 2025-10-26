package com.example.smartgym.data.repository

import com.example.smartgym.data.model.AuthResponse
import com.example.smartgym.data.model.RegisterResponse
import com.example.smartgym.data.network.AuthApiService
import com.example.smartgym.data.network.LoginRequest
import com.example.smartgym.data.network.RegisterRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {

    override suspend fun login(loginRequest: LoginRequest): AuthResponse? {
        return try {
            val response = api.login(loginRequest)
            if (response.isSuccessful) {
                response.body() // Retorna o AuthResponse (com o token)
            } else {

                null
            }
        } catch (e: Exception) {

            null
        }
    }

    override suspend fun register(registerRequest: RegisterRequest): RegisterResponse? {
        return try {
            val response = api.register(registerRequest)
            if (response.isSuccessful) {
                response.body()
            } else {

                null
            }
        } catch (e: Exception) {

            null
        }
    }
}