package com.example.smartgym.data.repository

import android.util.Log
import com.example.smartgym.data.network.LoginRequest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AuthRepositoryTest {


    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var repository: AuthRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun login_success_returns_token() = runBlocking {

        val loginRequest = LoginRequest(
            email = "user@smartgym_2.com",
            password = "senha1234"
        )

        val response = repository.login(loginRequest)

        Log.d("AuthRepositoryTest", "Resposta da API: $response")

        assertThat(response).isNotNull()

        assertThat(response?.accessToken).isNotEmpty()
    }
}