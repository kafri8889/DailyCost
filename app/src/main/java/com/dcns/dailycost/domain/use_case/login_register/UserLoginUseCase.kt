package com.dcns.dailycost.domain.use_case.login_register

import com.dcns.dailycost.data.model.networking.response.LoginResponse
import com.dcns.dailycost.domain.repository.ILoginRegisterRepository
import okhttp3.RequestBody
import retrofit2.Response

class UserLoginUseCase(
    private val loginRegisterRepository: ILoginRegisterRepository
) {

    suspend operator fun invoke(
        body: RequestBody
    ): Response<LoginResponse> {
        return loginRegisterRepository.login(body)
    }

}