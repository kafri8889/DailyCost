package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class IncomeResponseData(
	@SerializedName("jumlah") val amount: Int,
	@SerializedName("kategori") val category: String,
	@SerializedName("nama") val name: String,
	@SerializedName("pembayaran") val payment: String,
	@SerializedName("tanggal") val date: String,
	@SerializedName("pemasukan_id") val incomeId: Int,
	@SerializedName("user_id") val userId: Int
)