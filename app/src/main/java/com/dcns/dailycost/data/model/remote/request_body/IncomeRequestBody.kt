package com.dcns.dailycost.data.model.remote.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName

data class IncomeRequestBody(
    @SerializedName("jumlah") val amount: Int,
    @SerializedName("kategori") val category: String,
    @SerializedName("nama") val name: String,
    @SerializedName("pembayaran") val payment: String,
    @SerializedName("tanggal") val date: String,
    @SerializedName("user_id") val userId: Int
): RetrofitRequestBody() {

    override fun getBody(): Map<String, Any> {
        return mapOf(
            "jumlah" to amount,
            "kategori" to category,
            "nama" to name,
            "pembayaran" to payment,
            "tanggal" to date,
            "user_id" to userId
        )
    }
}