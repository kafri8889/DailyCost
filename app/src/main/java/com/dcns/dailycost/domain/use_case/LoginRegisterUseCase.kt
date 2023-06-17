package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.data.LoginRegisterType
import com.dcns.dailycost.data.model.networking.response.LoginResponse
import com.dcns.dailycost.data.repository.LoginRegisterRepository
import com.dcns.dailycost.foundation.common.IResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class LoginRegisterUseCase @Inject constructor(
    private val loginRegisterRepository: LoginRegisterRepository
) {

    /**
     * @return if type is [LoginRegisterType.Login] return [LoginResponse], [LoginRegisterType.Register] -> [RegisterResponse]
     */
    suspend operator fun invoke(
        type: LoginRegisterType,
        body: RequestBody
    ): Response<out IResponse> {
        return if (type == LoginRegisterType.Login) {
            loginRegisterRepository.login(body)
        } else loginRegisterRepository.register(body)
    }

}