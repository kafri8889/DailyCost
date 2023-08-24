package com.dcns.dailycost.data.model.remote

import com.google.gson.annotations.SerializedName

data class LoginResponseData(
	val id: Int,
	@SerializedName("nama") val name: String
)