package com.example.smartgym.viewmodel.profile


data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val goal: String = "",
    val isLoading: Boolean = false,
    val isLoadingSave: Boolean = false,
    val errorMessage: String? = null
)

sealed class ProfileUiEvent {
    data class ShowToast(val message: String) : ProfileUiEvent()

}