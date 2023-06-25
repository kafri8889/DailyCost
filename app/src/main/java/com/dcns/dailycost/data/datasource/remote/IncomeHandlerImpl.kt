package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.IncomeHandler
import com.dcns.dailycost.data.datasource.remote.services.IncomeService
import com.dcns.dailycost.data.model.remote.response.IncomeResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class IncomeHandlerImpl @Inject constructor(
    private val incomeService: IncomeService
): IncomeHandler {

    override suspend fun addIncome(body: RequestBody, token: String): Response<IncomeResponse> {
        return incomeService.addIncome(body, token)
    }

    override suspend fun getIncome(userId: Int, token: String): Response<IncomeResponse> {
        return incomeService.getIncome(userId, token)
    }
}