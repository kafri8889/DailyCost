package com.dcns.dailycost.data.model.networking

import com.google.gson.annotations.SerializedName

data class LoginResponseData(
    val id: Int,
    @SerializedName("nama") val name: String
)