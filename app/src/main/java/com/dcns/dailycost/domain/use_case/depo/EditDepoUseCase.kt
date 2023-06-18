package com.dcns.dailycost.domain.use_case.depo

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import com.dcns.dailycost.domain.repository.IDepoRepository
import okhttp3.RequestBody
import retrofit2.Response

class EditDepoUseCase(
    private val depoRepository: IDepoRepository
) {

    suspend operator fun invoke(
        body: RequestBody
    ): Response<DepoResponse> {
        return depoRepository.editDepo(body)
    }

}