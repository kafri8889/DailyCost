package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class DepoResponseData(
    @SerializedName("uang_cash") val cash: Int,
    @SerializedName("uang_gopay") val gopay: Int,
    @SerializedName("uang_rekening") val bankAccount: Int,
    @SerializedName("user_id") val userId: Int
)