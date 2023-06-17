package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.LoginRegisterHandler
import com.dcns.dailycost.data.model.networking.response.LoginResponse
import com.dcns.dailycost.data.model.networking.response.RegisterResponse
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class LoginRegisterRepository @Inject constructor(
    private val loginRegisterHandler: LoginRegisterHandler
): ILoginRegisterRepository {

    override suspend fun login(body: RequestBody): Response<LoginResponse> {
        return loginRegisterHandler.login(body)
    }

    override suspend fun register(body: RequestBody): Response<RegisterResponse> {
        return loginRegisterHandler.register(body)
    }
}