package com.dcns.dailycost.data.model.remote.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName

data class ShoppingRequestBody(
    @SerializedName("jumlah") val amount: Int,
    @SerializedName("nama") val name: String,
    @SerializedName("pembayaran") val payment: String,
    @SerializedName("kategori") val category: String,
    @SerializedName("tanggal") val date: String,
    @SerializedName("user_id") val userId: Int,
): RetrofitRequestBody() {

    override fun getBody(): Map<String, Any> {
        return mapOf(
            "jumlah" to amount,
            "nama" to name,
            "pembayaran" to payment,
            "kategori" to category,
            "tanggal" to date,
            "user_id" to userId,
        )
    }
}