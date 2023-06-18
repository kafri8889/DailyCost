package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.data.model.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRegisterService {

    @POST("/login")
    suspend fun login(@Body body: RequestBody): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body body: RequestBody): Response<RegisterResponse>

}