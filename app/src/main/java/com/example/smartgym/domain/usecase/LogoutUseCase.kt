package com.example.smartgym.domain.usecase

import com.example.smartgym.data.repository.TokenManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke() {
        tokenManager.clearToken()
    }
}

