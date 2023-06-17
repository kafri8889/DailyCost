package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.networking.response.LoginResponse
import com.dcns.dailycost.data.model.networking.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response

interface LoginRegisterHandler {

    suspend fun login(body: RequestBody): Response<LoginResponse>

    suspend fun register(body: RequestBody): Response<RegisterResponse>

}