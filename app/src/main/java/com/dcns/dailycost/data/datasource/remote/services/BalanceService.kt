package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.networking.response.BalanceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface BalanceService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/saldo/{id}")
    suspend fun getBalance(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<BalanceResponse>

}