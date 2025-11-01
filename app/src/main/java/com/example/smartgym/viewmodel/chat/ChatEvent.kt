package com.example.smartgym.viewmodel.chat

sealed class ChatEvent {
    data class OnMessageChanged(val text: String) : ChatEvent()
    data object OnSendClick : ChatEvent()
}