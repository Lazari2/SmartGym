package com.example.smartgym.domain.usecase

import com.example.smartgym.data.model.CreateWorkoutRequest
import com.example.smartgym.data.model.WorkoutDetailResponse
import com.example.smartgym.data.repository.TokenManager
import com.example.smartgym.data.repository.WorkoutRepository
import com.example.smartgym.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CreateWorkoutUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val tokenManager: TokenManager
) {
    operator fun invoke(request: CreateWorkoutRequest): Flow<Resource<WorkoutDetailResponse>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken().first()
            if (token == null) {
                emit(Resource.Error("Usuário não autenticado."))
                return@flow
            }

            val result = workoutRepository.createWorkout(token, request)
            emit(result)

        } catch (e: Exception) {
            emit(Resource.Error("Falha ao salvar o treino: ${e.message}"))
        }
    }
}