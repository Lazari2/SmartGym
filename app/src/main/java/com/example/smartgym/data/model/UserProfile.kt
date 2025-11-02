package com.example.smartgym.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val name: String,
    val email: String,
    val age: Int?,
    val weight: Double?,
    val height: Double?,
    val goal: String?
)

@Serializable
data class ProfileRequest(
    val name: String,
    val age: Int?,
    val weight: Double?,
    val height: Double?,
    val goal: String?
)

@Serializable
data class MessageResponse(
    val message: String
)