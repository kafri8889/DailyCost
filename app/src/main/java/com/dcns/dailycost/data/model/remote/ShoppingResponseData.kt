package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class ShoppingResponseData(
    @SerializedName("jumlah") val amount: Int,
    @SerializedName("nama") val name: String,
    @SerializedName("pembayaran") val payment: String,
    @SerializedName("tanggal") val date: String,
    @SerializedName("user_id") val userId: Int
)