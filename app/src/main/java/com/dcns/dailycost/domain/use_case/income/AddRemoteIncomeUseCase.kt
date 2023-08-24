package com.dcns.dailycost.domain.use_case.income

import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import com.dcns.dailycost.domain.repository.IIncomeRepository
import okhttp3.RequestBody
import retrofit2.Response

/**
 * Use case untuk menambahkan pemsaukan baru ke database server
 */
class AddRemoteIncomeUseCase(
	private val incomeRepository: IIncomeRepository
) {

	suspend operator fun invoke(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<IncomePostResponse> {
		return incomeRepository.addRemoteIncome(userId, body, token)
	}

}