package com.example.smartgym.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgym.data.model.WorkoutDetailResponse
import com.example.smartgym.domain.usecase.GetWorkoutsUseCase
import com.example.smartgym.domain.util.Resource
import com.example.smartgym.ui.components.WorkoutData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InitialScreenViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InitialScreenUiState())
    val uiState: StateFlow<InitialScreenUiState> = _uiState.asStateFlow()

    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))

    init {
        loadWorkouts()
    }

    fun onEvent(event: InitialScreenEvent) {
        when(event) {
            is InitialScreenEvent.OnWeekdaySelected -> {
                _uiState.update { currentState ->
                    val filteredList = currentState.allWorkouts.filter {
                        it.weekday == event.weekday
                    }
                    currentState.copy(
                        selectedWeekday = event.weekday,
                        filteredWorkouts = filteredList
                    )
                }
            }
        }
    }

     fun loadWorkouts() {
        getWorkoutsUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Success -> {
                    val workoutsDto = result.data ?: emptyList()

                    val workoutUiData = workoutsDto.map { dto -> mapToWorkoutData(dto) }

                    val trainedDates = workoutsDto.mapNotNull { dto ->
                        try {
                            LocalDateTime.parse(dto.created_at, isoFormatter).toLocalDate()
                        } catch (e: Exception) {
                            null
                        }
                    }
                    val streak = calculateOffensiveDays(trainedDates)
                    val selectedDay = _uiState.value.selectedWeekday

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allWorkouts = workoutUiData,
                            filteredWorkouts = workoutUiData.filter { it.weekday == selectedDay },
                            trainedDates = trainedDates,
                            offensiveDays = streak
                        )
                    }
                }
                is Resource.Error -> {  }
            }
        }.launchIn(viewModelScope)
    }

    private fun mapToWorkoutData(dto: WorkoutDetailResponse): WorkoutData {
        val formattedDate = try {
            val localDateTime = LocalDateTime.parse(dto.created_at, isoFormatter)
            localDateTime.format(displayFormatter)
        } catch (e: Exception) {
            dto.created_at.substring(0, 10)
        }
        return WorkoutData(
            name = dto.name,
            date = formattedDate,
            isNewRecord = false, // TODO: Lógica de recorde
            icon = getIconForWorkout(dto.name),
            weekday = dto.weekday,
            exercises = dto.exercises
        )
    }

    private fun getIconForWorkout(workoutName: String): ImageVector {
        return when {
            workoutName.contains("Peito", ignoreCase = true) -> Icons.AutoMirrored.Filled.TrendingUp
            workoutName.contains("Costas", ignoreCase = true) -> Icons.Default.Star
            workoutName.contains("Biceps", ignoreCase = true) -> Icons.Default.FitnessCenter
            else -> Icons.Default.FitnessCenter
        }
    }

    private fun calculateOffensiveDays(workoutDates: List<LocalDate>): Int {
        if (workoutDates.isEmpty()) {
            return 0
        }
        val sortedDates = workoutDates.sortedByDescending { it }
        var streak = 0
        var currentDate = LocalDate.now()
        if (sortedDates.first().isEqual(currentDate) || sortedDates.first().isEqual(currentDate.minusDays(1))) {
            streak = 1
            currentDate = sortedDates.first()
            for (i in 1 until sortedDates.size) {
                if (sortedDates[i].isEqual(currentDate.minusDays(1))) {
                    streak++
                    currentDate = sortedDates[i]
                } else if (sortedDates[i].isEqual(currentDate)) {
                    continue
                } else {
                    break
                }
            }
        }
        return streak
    }
}