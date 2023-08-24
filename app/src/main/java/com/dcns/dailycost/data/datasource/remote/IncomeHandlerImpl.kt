package com.dcns.dailycost.data.datasource.remote

import com.dcns.dailycost.data.datasource.remote.handlers.IncomeHandler
import com.dcns.dailycost.data.datasource.remote.services.IncomeService
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class IncomeHandlerImpl @Inject constructor(
	private val incomeService: IncomeService
): IncomeHandler {

	override suspend fun addIncome(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<IncomePostResponse> {
		return incomeService.addIncome(userId, body, token)
	}

	override suspend fun getIncome(userId: Int, token: String): Response<IncomeGetResponse> {
		return incomeService.getIncome(userId, token)
	}

	override suspend fun deleteIncome(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<DeleteResponse> {
		return incomeService.deleteIncome(userId, body, token)
	}
}