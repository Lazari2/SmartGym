package com.example.smartgym.viewmodel.addworkout

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgym.data.model.CreateWorkoutRequest
import com.example.smartgym.data.model.ExerciseRequest
import com.example.smartgym.data.model.ExerciseTemplateResponse
import com.example.smartgym.domain.usecase.CreateWorkoutUseCase
import com.example.smartgym.domain.usecase.GetExercisesByGroupUseCase
import com.example.smartgym.domain.usecase.GetMuscleGroupsUseCase
import com.example.smartgym.domain.util.Resource
import com.example.smartgym.viewmodel.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorkoutViewModel @Inject constructor(
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase,
    private val createWorkoutUseCase: CreateWorkoutUseCase,
    savedStateHandle: SavedStateHandle,

) : ViewModel() {

    private val _uiState = MutableStateFlow(AddWorkoutUiState())
    val uiState: StateFlow<AddWorkoutUiState> = _uiState.asStateFlow()
    private var templateLoadingJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val selectedWeekday: String = checkNotNull(savedStateHandle["weekday"])

    init {
        _uiState.update { it.copy(weekday = selectedWeekday) }
        loadMuscleGroups()
        templateLoadingJob = loadAllExerciseTemplates()
    }

    fun onEvent(event: AddWorkoutEvent) {
        when (event) {
            is AddWorkoutEvent.OnTitleChanged ->
                _uiState.update { it.copy(workoutTitle = event.title) }

            is AddWorkoutEvent.OnMuscleGroupSelected -> {
                _uiState.update { it.copy(selectedMuscleGroup = event.group) }
                loadExercisesForGroup(event.group)
            }

            is AddWorkoutEvent.OnExerciseTemplateSelected ->
                _uiState.update { it.copy(selectedTemplate = event.template) }

            is AddWorkoutEvent.OnSetsChanged -> _uiState.update { it.copy(currentSets = event.sets) }
            is AddWorkoutEvent.OnRepsChanged -> _uiState.update { it.copy(currentReps = event.reps) }
            is AddWorkoutEvent.OnWeightChanged -> _uiState.update { it.copy(currentWeight = event.weight) }
            is AddWorkoutEvent.OnNotesChanged -> _uiState.update { it.copy(currentNotes = event.notes) }

            is AddWorkoutEvent.OnAddExerciseToList -> addExerciseToList()

            is AddWorkoutEvent.OnRemoveExercise ->
                _uiState.update { it.copy(addedExercises = it.addedExercises - event.exercise) }

            is AddWorkoutEvent.OnSaveWorkout -> saveWorkout()

            is AddWorkoutEvent.OnIaDataReceived -> {
                viewModelScope.launch {

                    Log.d("AddWorkoutVM", "Evento OnIaDataReceived. Esperando templates...")

                    templateLoadingJob?.join()

                    Log.d("AddWorkoutVM", "Templates prontos. Processando dados da IA...")

                    processIaData(event.exercises)
                }
            }
        }
    }

    private fun loadMuscleGroups() {
        getMuscleGroupsUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isLoadingGroups = true) }
                is Resource.Success -> {
                    val groups = result.data ?: emptyList()

                    Log.d("ViewModelData", "Grupos musculares recebidos com SUCESSO: $groups")

                    _uiState.update { it.copy(isLoadingGroups = false, muscleGroups = groups) }
                }
                is Resource.Error -> {
                    Log.e("ViewModelData", "Erro ao buscar grupos: ${result.message}")
                    _uiState.update { it.copy(isLoadingGroups = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun loadAllExerciseTemplates(): Job {
        return viewModelScope.launch {
            val allTemplates = mutableListOf<ExerciseTemplateResponse>()
            val groupsResult = getMuscleGroupsUseCase()
                .filter { it !is Resource.Loading }
                .first()

            if (groupsResult is Resource.Success) {
                Log.d("AddWorkoutVM", "Busca de grupos OK. Buscando exercícios...")

                groupsResult.data?.forEach { groupName ->

                    val exercisesResult = getExercisesByGroupUseCase(groupName)
                        .filter { it !is Resource.Loading }
                        .first()

                    if (exercisesResult is Resource.Success) {
                        allTemplates.addAll(exercisesResult.data ?: emptyList())
                    }
                }

                _uiState.update { it.copy(allExerciseTemplates = allTemplates) }
                Log.d("AddWorkoutVM", "Carregou ${allTemplates.size} templates de exercício para o RAG.")

            } else if (groupsResult is Resource.Error) {
                Log.e("AddWorkoutVM", "Falha ao carregar grupos para o RAG.")
            }
        }
    }
    private fun loadExercisesForGroup(groupName: String) {
        getExercisesByGroupUseCase(groupName).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isLoadingExercises = true) }
                is Resource.Success -> _uiState.update { it.copy(isLoadingExercises = false, exercisesForGroup = result.data ?: emptyList()) }
                is Resource.Error -> _uiState.update { it.copy(isLoadingExercises = false, errorMessage = result.message) }
            }
        }.launchIn(viewModelScope)
    }

    private fun addExerciseToList() {
        val state = _uiState.value
        val template = state.selectedTemplate
        val sets = state.currentSets.toIntOrNull()
        val reps = state.currentReps.toIntOrNull()

        if (template == null) {
            _uiState.update { it.copy(errorMessage = "Selecione um exercício.") }
            return
        }
        if (sets == null || sets <= 0 || reps == null || reps <= 0) {
            _uiState.update { it.copy(errorMessage = "Sets e Reps são obrigatórios.") }
            return
        }

        val newExercise = AddedExercise(
            templateId = template.id,
            name = template.name,
            sets = sets,
            reps = reps,
            weight = state.currentWeight.toDoubleOrNull(),
            notes = state.currentNotes.takeIf { it.isNotBlank() }
        )

        _uiState.update {
            it.copy(
                addedExercises = it.addedExercises + newExercise,
                selectedTemplate = null,
                currentSets = "",
                currentReps = "",
                currentWeight = "",
                currentNotes = ""
            )
        }
    }

    private fun saveWorkout() {
        val state = _uiState.value

        if (state.workoutTitle.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Título do treino é obrigatório.") }
            return
        }
        if (state.addedExercises.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Adicione pelo menos um exercício.") }
            return
        }

        val exerciseRequests = state.addedExercises.map {
            ExerciseRequest(
                templateId = it.templateId,
                sets = it.sets,
                reps = it.reps,
                weight = it.weight,
                notes = it.notes
            )
        }

        val request = CreateWorkoutRequest(
            name = state.workoutTitle,
            weekday = state.weekday,
            exercises = exerciseRequests
        )

        createWorkoutUseCase(request).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isSaving = true) }
                is Resource.Success -> {
                    _uiState.update { it.copy(isSaving = false) }
                    viewModelScope.launch {
                        Log.d("AddWorkoutVM", "Salvou com sucesso! Emitindo ShowToast e NavigateSaveBack(true)")
                        _eventFlow.emit(UiEvent.ShowToast("Treino salvo com sucesso!"))
                        _eventFlow.emit(UiEvent.NavigateSaveBack(saved = true))

                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isSaving = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }
    private fun processIaData(iaExercises: List<ExerciseRequest>) {
        val allTemplates = _uiState.value.allExerciseTemplates

        if (allTemplates.isEmpty()) {
            Log.e("AddWorkoutVM", "A lista de templates está vazia. Não é possível processar os dados da IA.")
            _uiState.update { it.copy(errorMessage = "Erro: A biblioteca de exercícios não está carregada.") }
            return
        }

        val newAddedExercises = iaExercises.mapNotNull { iaExercise ->

            val template = allTemplates.find { it.id == iaExercise.templateId }

            if (template != null) {

                AddedExercise(
                    templateId = template.id,
                    name = template.name,
                    sets = iaExercise.sets,
                    reps = iaExercise.reps,
                    weight = iaExercise.weight,
                    notes = iaExercise.notes
                )
            } else {
                Log.w("AddWorkoutVM", "IA retornou um template_id desconhecido: ${iaExercise.templateId}")
                null
            }
        }

        Log.d("AddWorkoutVM", "LOG ANTES: Exercícios na lista: ${_uiState.value.addedExercises.size}")
        Log.d("AddWorkoutVM", "LOG IA: Exercícios recebidos da IA: ${newAddedExercises.size}")
        _uiState.update {
            it.copy(addedExercises = newAddedExercises)
        }

        viewModelScope.launch {
            _eventFlow.emit(UiEvent.ShowToast("${newAddedExercises.size} exercícios adicionados pela IA!"))
        }
    }

}