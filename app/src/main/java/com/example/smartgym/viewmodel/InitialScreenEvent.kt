package com.example.smartgym.viewmodel

sealed class InitialScreenEvent {

    data class OnWeekdaySelected(val weekday: String) : InitialScreenEvent()
}