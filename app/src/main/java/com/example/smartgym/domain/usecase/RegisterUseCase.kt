package com.example.smartgym.domain.usecase

import com.example.smartgym.data.model.RegisterResponse
import com.example.smartgym.data.network.RegisterRequest
import com.example.smartgym.data.repository.AuthRepository
import com.example.smartgym.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(request: RegisterRequest): Flow<Resource<RegisterResponse>> = flow {
        try {
            emit(Resource.Loading())

            if (request.password != request.confirm_password) {
                emit(Resource.Error("As senhas não conferem."))
                return@flow
            }

            val response = repository.register(request)

            if (response != null) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error("Não foi possível criar a conta."))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Não foi possível conectar ao servidor. Tente novamente."))
        }
    }
}