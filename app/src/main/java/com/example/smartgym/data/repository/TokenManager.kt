package com.example.smartgym.data.repository

import kotlinx.coroutines.flow.Flow

interface TokenManager {
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    suspend fun clearToken()
}