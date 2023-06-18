package com.dcns.dailycost.data.model.remote.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class ShoppingRequestBody(
    @SerializedName("jumlah") val amount: Int,
    @SerializedName("nama") val name: String,
    @SerializedName("pembayaran") val payment: String,
    @SerializedName("tanggal") val date: String,
    @SerializedName("user_id") val userId: Int,
): RetrofitRequestBody {

    override fun toRequestBody(): RequestBody {
        val body = JSONObject(
            mapOf(
                "jumlah" to amount,
                "nama" to name,
                "pembayaran" to payment,
                "tanggal" to date,
                "user_id" to userId,
            )
        )

        return body
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}