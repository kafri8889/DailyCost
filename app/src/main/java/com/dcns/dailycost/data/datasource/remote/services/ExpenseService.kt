package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ExpenseService {

    /*
    Untuk API get pengeluaran chart sama by date kayaknya ngga dipake,
    Jadi untuk proses datanya (u/ get by date sama tanggal) ngambil dari database (custom sendiri)
     */

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("/api/pengeluaran/{id}")
    suspend fun addExpense(
        @Path("id") userId: Int,
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Response<AddExpenseResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("/api/pengeluaran/{id}")
    suspend fun getExpense(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Response<GetExpenseResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @HTTP(method = "DELETE", path = "/api/pengeluaran/{id}", hasBody = true)
    suspend fun deleteExpense(
        @Path("id") userId: Int,
        @Body body: RequestBody,
        @Header("Authorization") token: String
    ): Response<DeleteResponse>

}