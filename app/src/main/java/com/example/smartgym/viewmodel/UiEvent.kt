package com.example.smartgym.viewmodel
sealed class UiEvent {

    data class ShowToast(val message: String) : UiEvent()

    data object NavigateToHome : UiEvent()
    data object NavigateBack : UiEvent()
    data class NavigateSaveBack(val saved: Boolean = false) : UiEvent()

    data class RegisterSuccess(val message: String) : UiEvent()
}