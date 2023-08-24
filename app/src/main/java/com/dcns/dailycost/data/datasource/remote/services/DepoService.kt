package com.dcns.dailycost.data.datasource.remote.services

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DepoService {

	@Headers("Content-Type: application/json;charset=UTF-8")
	@GET("/api/saldo/{id}")
	suspend fun getBalance(
		@Path("id") userId: Int,
		@Header("Authorization") token: String
	): Response<DepoResponse>

	@Headers("Content-Type: application/json;charset=UTF-8")
	@PUT("/api/depo")
	suspend fun editDepo(
		@Body body: RequestBody,
		@Header("Authorization") token: String
	): Response<DepoResponse>

	@Headers("Content-Type: application/json;charset=UTF-8")
	@POST("/api/depo")
	suspend fun addDepo(
		@Body body: RequestBody,
		@Header("Authorization") token: String
	): Response<DepoResponse>

	@Headers("Content-Type: application/json;charset=UTF-8")
	@POST("/api/topup")
	suspend fun topup(
		@Body body: RequestBody,
		@Header("Authorization") token: String
	): Response<DepoResponse>

}