package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.networking.response.ShoppingResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ShoppingService {

    @POST("/api/belanja")
    suspend fun shopping(@Body body: RequestBody): Response<ShoppingResponse>

}