package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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
    ): Response<IncomePostResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/pemasukan/{id}")
    suspend fun getIncome(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<IncomeGetResponse>

}