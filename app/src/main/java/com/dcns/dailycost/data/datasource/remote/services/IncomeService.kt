package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.IncomeResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface IncomeService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/pemasukan")
    suspend fun addIncome(
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Response<IncomeResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/pemasukan/{id}")
    suspend fun getIncome(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<IncomeResponse>

}