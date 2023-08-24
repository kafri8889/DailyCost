package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class GetExpenseResponseResult(
	@SerializedName("jumlah") val amount: Int,
	@SerializedName("kategori") val category: String,
	@SerializedName("nama") val name: String,
	@SerializedName("pembayaran") val payment: String,
	@SerializedName("pengeluaran_id") val id: Int,
	@SerializedName("tanggal") val date: String
)