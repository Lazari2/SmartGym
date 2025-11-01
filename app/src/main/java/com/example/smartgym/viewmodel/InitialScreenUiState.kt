package com.example.smartgym.viewmodel

import com.example.smartgym.ui.components.WorkoutData
import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle

data class InitialScreenUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val offensiveDays: Int = 0,
    val selectedWeekday: String = getTodayWeekday(),
    val allWorkouts: List<WorkoutData> = emptyList(),
    val filteredWorkouts: List<WorkoutData> = emptyList(),
    val trainedDates: List<LocalDate> = emptyList()
)

private fun getTodayWeekday(): String {
    val locale = Locale.forLanguageTag("pt-BR")
    return LocalDate.now()
        .dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}