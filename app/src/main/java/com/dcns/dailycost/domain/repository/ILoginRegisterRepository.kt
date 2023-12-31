package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.data.model.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response

interface ILoginRegisterRepository {

	/**
	 * Login
	 */
	suspend fun login(body: RequestBody): Response<LoginResponse>

	/**
	 * Register
	 */
	suspend fun register(body: RequestBody): Response<RegisterResponse>

}