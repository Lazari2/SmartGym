package com.example.smartgym.data.repository

import com.example.smartgym.data.model.AuthResponse
import com.example.smartgym.data.model.RegisterResponse
import com.example.smartgym.data.network.LoginRequest
import com.example.smartgym.data.network.RegisterRequest
import com.example.smartgym.domain.util.Resource


interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Resource<AuthResponse>
    suspend fun register(registerRequest: RegisterRequest): Resource<RegisterResponse>
}