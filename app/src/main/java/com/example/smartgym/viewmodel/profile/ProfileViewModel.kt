package com.example.smartgym.viewmodel.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartgym.data.model.ProfileRequest
import com.example.smartgym.domain.usecase.GetProfileUseCase
import com.example.smartgym.domain.usecase.UpdateProfileUseCase
import com.example.smartgym.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ProfileUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnNameChanged -> _uiState.update { it.copy(name = event.value) }
            is ProfileEvent.OnAgeChanged -> _uiState.update { it.copy(age = event.value) }
            is ProfileEvent.OnWeightChanged -> _uiState.update { it.copy(weight = event.value) }
            is ProfileEvent.OnHeightChanged -> _uiState.update { it.copy(height = event.value) }
            is ProfileEvent.OnGoalChanged -> _uiState.update { it.copy(goal = event.value) }

            ProfileEvent.OnSaveClicked -> saveProfile()
            ProfileEvent.OnCancelEdit -> {
                loadProfile()
            }
        }
    }

    private fun loadProfile() {
        getProfileUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Success -> {
                    val profile = result.data
                    Log.d("ProfileViewModel", "Perfil carregado: $profile")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            name = profile?.name ?: "",
                            email = profile?.email ?: "",
                            age = profile?.age?.toString() ?: "",
                            weight = profile?.weight?.toString() ?: "",
                            height = profile?.height?.toString() ?: "",
                            goal = profile?.goal ?: ""
                        )
                    }
                }
                is Resource.Error -> {
                    Log.e("ProfileViewModel", "Erro ao carregar perfil: ${result.message}")
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveProfile() {
        val currentState = _uiState.value

        if (currentState.name.isBlank()) {
            viewModelScope.launch {
                _eventFlow.emit(ProfileUiEvent.ShowToast("O nome não pode ficar em branco."))
            }
            return
        }

        val request = ProfileRequest(
            name = currentState.name,
            age = currentState.age.toIntOrNull(),
            weight = currentState.weight.toDoubleOrNull(),
            height = currentState.height.toDoubleOrNull(),
            goal = currentState.goal.takeIf { it.isNotBlank() }
        )
        Log.d("ProfileViewModel", "Enviando para a API (PUT): $request")
        updateProfileUseCase(request).onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isLoadingSave = true) }
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoadingSave = false) }
                    Log.d("ProfileViewModel", "Perfil salvo com sucesso.")
                    _eventFlow.emit(ProfileUiEvent.ShowToast("Perfil salvo com sucesso!"))
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoadingSave = false) }
                    Log.e("ProfileViewModel", "Erro ao salvar: ${result.message}")
                    _eventFlow.emit(ProfileUiEvent.ShowToast(result.message ?: "Erro ao salvar"))
                }
            }
        }.launchIn(viewModelScope)
    }
}