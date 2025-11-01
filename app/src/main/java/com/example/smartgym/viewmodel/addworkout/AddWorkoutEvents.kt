package com.example.smartgym.viewmodel.addworkout

import com.example.smartgym.data.model.ExerciseTemplateResponse
import com.example.smartgym.data.model.ExerciseRequest
sealed class AddWorkoutEvent {

    data class OnTitleChanged(val title: String) : AddWorkoutEvent()

    data class OnMuscleGroupSelected(val group: String) : AddWorkoutEvent()

    data class OnExerciseTemplateSelected(val template: ExerciseTemplateResponse) : AddWorkoutEvent()
    data class OnSetsChanged(val sets: String) : AddWorkoutEvent()
    data class OnRepsChanged(val reps: String) : AddWorkoutEvent()
    data class OnWeightChanged(val weight: String) : AddWorkoutEvent()
    data class OnNotesChanged(val notes: String) : AddWorkoutEvent()

    data object OnAddExerciseToList : AddWorkoutEvent()
    data class OnRemoveExercise(val exercise: AddedExercise) : AddWorkoutEvent()
    data object OnSaveWorkout : AddWorkoutEvent()
    data class OnIaDataReceived(val exercises: List<ExerciseRequest>) : AddWorkoutEvent()
}

sealed class AddWorkoutUiEvent {
    data object SaveSuccess : AddWorkoutUiEvent()
}