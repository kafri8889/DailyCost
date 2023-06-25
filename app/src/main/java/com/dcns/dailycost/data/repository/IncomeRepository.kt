package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.remote.handlers.IncomeHandler
import com.dcns.dailycost.data.model.remote.response.IncomeResponse
import com.dcns.dailycost.domain.repository.IIncomeRepository
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val incomeHandler: IncomeHandler
): IIncomeRepository {

    override suspend fun addIncome(body: RequestBody, token: String): Response<IncomeResponse> {
        return incomeHandler.addIncome(body, token)
    }

    override suspend fun getIncome(userId: Int, token: String): Response<IncomeResponse> {
        return incomeHandler.getIncome(userId, token)
    }
}