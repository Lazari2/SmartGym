package com.example.smartgym.data.repository


import com.example.smartgym.data.model.MessageResponse
import com.example.smartgym.data.model.ProfileRequest
import com.example.smartgym.data.model.ProfileResponse
import com.example.smartgym.domain.util.Resource


interface ProfileRepository {

    suspend fun getProfile(token: String): Resource<ProfileResponse>


    suspend fun updateProfile(token: String, request: ProfileRequest): Resource<MessageResponse>
}