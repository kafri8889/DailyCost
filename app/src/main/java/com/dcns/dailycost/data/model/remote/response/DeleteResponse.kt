package com.dcns.dailycost.data.model.remote.response

import com.dcns.dailycost.foundation.common.IResponse
import com.google.gson.annotations.SerializedName

data class DeleteResponse(
	@SerializedName("status") val status: String,
	@SerializedName("message") val message: String
): IResponse
