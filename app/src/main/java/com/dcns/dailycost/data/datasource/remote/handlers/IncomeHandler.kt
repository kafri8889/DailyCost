package com.dcns.dailycost.data.datasource.remote.handlers

import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import okhttp3.RequestBody
import retrofit2.Response

interface IncomeHandler {

	/**
	 * Tambah pemasukan baru
	 */
	suspend fun addIncome(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<IncomePostResponse>

	/**
	 * Dapatkan pemasukan dari id yang diberikan
	 */
	suspend fun getIncome(
		userId: Int,
		token: String
	): Response<IncomeGetResponse>

	/**
	 * Hapus pemasukan
	 */
	suspend fun deleteIncome(
		userId: Int,
		body: RequestBody,
		token: String
	): Response<DeleteResponse>

}