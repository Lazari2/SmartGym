package com.example.smartgym.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class RegisterResponse(
    @SerialName("message")
    val message: String,
    @SerialName("user")
    val user: UserResponse
)