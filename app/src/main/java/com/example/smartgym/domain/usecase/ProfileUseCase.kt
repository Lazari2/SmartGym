package com.example.smartgym.domain.usecase

import com.example.smartgym.data.model.ProfileRequest
import com.example.smartgym.data.model.ProfileResponse
import com.example.smartgym.data.repository.ProfileRepository
import com.example.smartgym.data.repository.TokenManager
import com.example.smartgym.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    private val tokenManager: TokenManager
) {
    operator fun invoke(): Flow<Resource<ProfileResponse>> = flow {
        emit(Resource.Loading())
        val token = tokenManager.getToken().first()
        if (token == null) {
            emit(Resource.Error("Usuário não autenticado."))
            return@flow
        }
        val result = repository.getProfile(token)
        emit(result)
    }
}

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
    private val tokenManager: TokenManager
) {
    operator fun invoke(request: ProfileRequest): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val token = tokenManager.getToken().first()
        if (token == null) {
            emit(Resource.Error("Usuário não autenticado."))
            return@flow
        }
        val result = repository.updateProfile(token, request)
        if (result is Resource.Success) {
            emit(Resource.Success(Unit))
        } else if (result is Resource.Error) {
            emit(Resource.Error(result.message ?: "Erro desconhecido"))
        }
    }
}