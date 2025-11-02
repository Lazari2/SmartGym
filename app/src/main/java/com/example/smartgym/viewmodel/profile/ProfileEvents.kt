package com.example.smartgym.viewmodel.profile

sealed class ProfileEvent {
    data class OnNameChanged(val value: String) : ProfileEvent()
    data class OnAgeChanged(val value: String) : ProfileEvent()
    data class OnWeightChanged(val value: String) : ProfileEvent()
    data class OnHeightChanged(val value: String) : ProfileEvent()
    data class OnGoalChanged(val value: String) : ProfileEvent()

    data object OnSaveClicked : ProfileEvent()
    data object OnCancelEdit : ProfileEvent()
}