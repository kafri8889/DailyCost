package com.dcns.dailycost.data.model.remote.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import com.google.gson.annotations.SerializedName

data class DepoRequestBody(
    @SerializedName("id") val id: Int,
    @SerializedName("uang_cash") val cash: Int,
    @SerializedName("uang_gopay") val eWallet: Int,
    @SerializedName("uang_rekening") val bankAccount: Int
): RetrofitRequestBody() {

    override fun getBody(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "uang_cash" to cash,
            "uang_gopay" to eWallet,
            "uang_rekening" to bankAccount,
        )
    }
}