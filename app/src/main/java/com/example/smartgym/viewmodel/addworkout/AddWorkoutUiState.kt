package com.example.smartgym.viewmodel.addworkout

import com.example.smartgym.data.model.ExerciseTemplateResponse

data class AddedExercise(
    val templateId: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double?,
    val notes: String?
)

data class AddWorkoutUiState(
    val workoutTitle: String = "",
    val weekday: String = "",

    val isLoadingGroups: Boolean = false,
    val muscleGroups: List<String> = emptyList(),
    val selectedMuscleGroup: String? = null,

    val isLoadingExercises: Boolean = false,
    val exercisesForGroup: List<ExerciseTemplateResponse> = emptyList(),

    val selectedTemplate: ExerciseTemplateResponse? = null,
    val currentSets: String = "",
    val currentReps: String = "",
    val currentWeight: String = "",
    val currentNotes: String = "",
    val allExerciseTemplates: List<ExerciseTemplateResponse> = emptyList(),
    val addedExercises: List<AddedExercise> = emptyList(),

    val isSaving: Boolean = false,
    val errorMessage: String? = null
)