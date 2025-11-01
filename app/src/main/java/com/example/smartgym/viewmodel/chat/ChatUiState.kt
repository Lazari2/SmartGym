package com.example.smartgym.viewmodel.chat

import com.example.smartgym.data.model.ExerciseRequest

import java.util.UUID

enum class Sender { USER, AI }
data class Message(val text: String, val sender: Sender, val id: UUID = UUID.randomUUID())

data class ChatUiState(
    val messageText: String = "",
    val messages: List<Message> = listOf(
        Message("Olá! Me diga que tipo de treino você quer montar.", Sender.AI)
    ),
    val isAiLoading: Boolean = false,
    val generatedWorkoutData: List<ExerciseRequest>? = null
)