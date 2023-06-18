package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface DepoService {

    @PUT("/api/depo")
    suspend fun editDepo(
        @Body body: RequestBody
    ): Response<DepoResponse>

    @POST("/api/depo")
    suspend fun addDepo(
        @Body body: RequestBody
    ): Response<DepoResponse>

    @POST("/api/topup")
    suspend fun topup(
        @Body body: RequestBody
    ): Response<DepoResponse>

}