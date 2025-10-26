package com.example.smartgym.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class InitialScreenViewModel : ViewModel() {

    private val _offensiveDays = MutableStateFlow(0)
    val offensiveDays: StateFlow<Int> = _offensiveDays

    init {
        calculateOffensiveDays()
    }

    private fun calculateOffensiveDays() {
        // Sample workout dates. In a real app, this would come from a database.
        val workoutDates = listOf(
            LocalDate.now(),
            LocalDate.now().minusDays(1),
            LocalDate.now().minusDays(2),
            LocalDate.now().minusDays(4), // Streak broken here
            LocalDate.now().minusDays(5),
        )

        if (workoutDates.isEmpty()) {
            _offensiveDays.value = 0
            return
        }

        val sortedDates = workoutDates.sortedByDescending { it }
        var streak = 0
        var currentDate = LocalDate.now()

        // Check if the most recent workout was today or yesterday
        if (sortedDates.first().isEqual(currentDate) || sortedDates.first().isEqual(currentDate.minusDays(1))){
            streak = 1
            currentDate = sortedDates.first()

            for (i in 1 until sortedDates.size) {
                if (sortedDates[i].isEqual(currentDate.minusDays(1))) {
                    streak++
                    currentDate = sortedDates[i]
                } else {
                    break // Streak is broken
                }
            }
        }

        _offensiveDays.value = streak
    }
}
