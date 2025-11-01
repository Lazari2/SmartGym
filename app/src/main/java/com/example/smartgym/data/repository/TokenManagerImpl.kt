package com.example.smartgym.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TokenManagerImpl @Inject constructor(
    private val context: Context
) : TokenManager {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("smartgym_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val AUTH_TOKEN_KEY = "auth_token"
    }

    override suspend fun saveToken(token: String) {
        prefs.edit().putString(AUTH_TOKEN_KEY, token).apply()
    }

    override fun getToken(): Flow<String?> = flow {
        emit(prefs.getString(AUTH_TOKEN_KEY, null))
    }

    override suspend fun clearToken() {
        prefs.edit().remove(AUTH_TOKEN_KEY).apply()
    }
}