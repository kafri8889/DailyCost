package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.DepoHandler
import com.dcns.dailycost.data.datasource.remote.services.DepoService
import com.dcns.dailycost.data.model.remote.response.DepoResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class DepoHandlerImpl @Inject constructor(
    private val depoService: DepoService
): DepoHandler {

    override suspend fun editDepo(body: RequestBody, token: String): Response<DepoResponse> {
        return depoService.editDepo(body, token)
    }

    override suspend fun addDepo(body: RequestBody, token: String): Response<DepoResponse> {
        return depoService.addDepo(body, token)
    }

    override suspend fun topup(body: RequestBody, token: String): Response<DepoResponse> {
        return depoService.topup(body, token)
    }
}