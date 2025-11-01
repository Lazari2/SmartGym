package com.example.smartgym.domain.usecase

import com.example.smartgym.data.repository.TokenManager
import com.example.smartgym.data.repository.WorkoutRepository
import com.example.smartgym.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlinx.coroutines.flow.catch

class GetMuscleGroupsUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val tokenManager: TokenManager
) {
    operator fun invoke(): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())

        val token = tokenManager.getToken().first()
        if (token == null) {
            emit(Resource.Error("Usuário não autenticado."))
            return@flow
        }

        val result = workoutRepository.getMuscleGroups(token)
        emit(result)

    }
        .catch { e ->
            emit(Resource.Error("Falha ao buscar grupos musculares: ${e.message}"))
        }
}