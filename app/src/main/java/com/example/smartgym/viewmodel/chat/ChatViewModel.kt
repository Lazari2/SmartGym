package com.example.smartgym.viewmodel.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgym.domain.usecase.GenerateWorkoutFromIaUseCase
import com.example.smartgym.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.example.smartgym.data.model.ExerciseRequest
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val generateWorkoutUseCase: GenerateWorkoutFromIaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()


    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnMessageChanged -> {
                _uiState.update { it.copy(messageText = event.text) }
            }
            is ChatEvent.OnSendClick -> {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val userPrompt = _uiState.value.messageText
        if (userPrompt.isBlank()) return

        val userMessage = Message(userPrompt, Sender.USER)
        _uiState.update {
            it.copy(
                messageText = "",
                messages = it.messages + userMessage,
                isAiLoading = true
            )
        }

        generateWorkoutUseCase(userPrompt).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val iaResponse = result.data!!
                    val aiMessage = Message(iaResponse.chatResponse, Sender.AI)
                    Log.d(
                        "ChatViewModel",
                        "Dados da IA recebidos e salvos no uiState: ${iaResponse.workoutData}"
                    )
                    _uiState.update {
                        it.copy(
                            isAiLoading = false,
                            messages = it.messages + aiMessage,
                            generatedWorkoutData = iaResponse.workoutData
                        )
                    }

                }
                is Resource.Error -> {
                    val errorMessage = Message(result.message ?: "Erro de IA", Sender.AI)
                    _uiState.update {
                        it.copy(
                            isAiLoading = false,
                            messages = it.messages + errorMessage
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isAiLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }
}