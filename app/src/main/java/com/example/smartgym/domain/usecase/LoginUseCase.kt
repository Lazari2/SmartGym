package com.example.smartgym.domain.usecase

import com.example.smartgym.data.model.AuthResponse
import com.example.smartgym.data.network.LoginRequest
import com.example.smartgym.data.repository.AuthRepository
import com.example.smartgym.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(request: LoginRequest): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = repository.login(request)

            if (response != null) {
                emit(Resource.Success(response))
            } else {
                // TODO: Ler o 'errorBody' do repositório para uma msg melhor
                emit(Resource.Error("Email ou senha inválidos."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Não foi possível conectar ao servidor. Tente novamente."))
        }
    }
}