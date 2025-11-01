package com.example.smartgym.viewmodel.chat

import com.example.smartgym.data.model.ExerciseRequest

sealed class ChatUiEvent {
    data class GenerationSuccess(val workoutData: List<ExerciseRequest>) : ChatUiEvent()
}