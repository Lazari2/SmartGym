package com.example.smartgym.data.repository

import android.util.Log
import com.example.smartgym.data.model.MessageResponse
import com.example.smartgym.data.model.ProfileRequest
import com.example.smartgym.data.model.ProfileResponse
import com.example.smartgym.data.network.AuthApiService
import com.example.smartgym.domain.util.Resource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : ProfileRepository {

    override suspend fun getProfile(token: String): Resource<ProfileResponse> {
        return try {
            val response = api.getProfile(authHeader = "Bearer $token")
            Resource.Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "Erro HTTP"
            Log.e("ProfileRepo", "getProfile HttpException: $errorBody", e)
            Resource.Error(parseErrorMessage(errorBody, "Falha ao carregar perfil"))
        } catch (e: IOException) {
            Log.e("ProfileRepo", "getProfile IOException", e)
            Resource.Error("Erro de conexão. Verifique sua rede.")
        } catch (e: Exception) {
            Log.e("ProfileRepo", "getProfile Exception", e)
            Resource.Error("Um erro inesperado ocorreu.")
        }
    }

    override suspend fun updateProfile(token: String, request: ProfileRequest): Resource<MessageResponse> {
        return try {
            val response = api.updateProfile(authHeader = "Bearer $token", request = request)
            Resource.Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "Erro HTTP"
            Log.e("ProfileRepo", "updateProfile HttpException: $errorBody", e)
            Resource.Error(parseErrorMessage(errorBody, "Falha ao salvar perfil"))
        } catch (e: IOException) {
            Log.e("ProfileRepo", "updateProfile IOException", e)
            Resource.Error("Erro de conexão. Verifique sua rede.")
        } catch (e: Exception) {
            Log.e("ProfileRepo", "updateProfile Exception", e)
            Resource.Error("Um erro inesperado ocorreu.")
        }
    }

    private fun parseErrorMessage(errorBody: String, defaultMessage: String): String {
        return try {
            val jsonObject = Json.parseToJsonElement(errorBody).jsonObject
            jsonObject["error"]?.jsonPrimitive?.content ?: defaultMessage
        } catch (e: Exception) {
            defaultMessage
        }
    }
}