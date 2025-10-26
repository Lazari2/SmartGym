package com.example.smartgym.data.network

import com.example.smartgym.data.model.AuthResponse
import com.example.smartgym.data.model.RegisterResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Estas classes definem o "corpo" (Body) que VAMOS ENVIAR
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

// Esta é a interface da API
interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // NOTA: Ainda não criamos o endpoint /me no back-end para GET,
    // mas quando criarmos, ele virá aqui.
}