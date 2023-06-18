package com.dcns.dailycost.data.model.networking.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class DepoRequestBody(
    @SerializedName("id") val id: Int,
    @SerializedName("uang_cash") val cash: Int,
    @SerializedName("uang_gopay") val gopay: Int,
    @SerializedName("uang_rekening") val bankAccount: Int
): RetrofitRequestBody {

    override fun toRequestBody(): RequestBody {
        val body = JSONObject(
            mapOf(
                "id" to id,
                "uang_cash" to cash,
                "uang_gopay" to gopay,
                "uang_rekening" to bankAccount,
            )
        )

        return body
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}