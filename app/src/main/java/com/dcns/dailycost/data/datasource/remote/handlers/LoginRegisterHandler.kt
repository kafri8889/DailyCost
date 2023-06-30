package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.data.model.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response

interface LoginRegisterHandler {

    /**
     * Login
     */
    suspend fun login(body: RequestBody): Response<LoginResponse>

    /**
     * Register
     */
    suspend fun register(body: RequestBody): Response<RegisterResponse>

}