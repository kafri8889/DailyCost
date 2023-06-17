package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.LoginRegisterHandler
import com.dcns.dailycost.data.datasource.remote.services.LoginRegisterService
import com.dcns.dailycost.data.model.networking.response.LoginResponse
import com.dcns.dailycost.data.model.networking.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class LoginRegisterHandlerImpl @Inject constructor(
    private val loginRegisterService: LoginRegisterService
): LoginRegisterHandler {

    override suspend fun login(body: RequestBody): Response<LoginResponse> {
        return loginRegisterService.login(body)
    }

    override suspend fun register(body: RequestBody): Response<RegisterResponse> {
        return loginRegisterService.register(body)
    }
}