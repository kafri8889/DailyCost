package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.ShoppingResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ShoppingService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/belanja")
    suspend fun shopping(
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Response<ShoppingResponse>

}