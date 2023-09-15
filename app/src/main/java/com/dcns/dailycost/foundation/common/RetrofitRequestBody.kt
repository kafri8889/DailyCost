package com.dcns.dailycost.foundation.common

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

abstract class RetrofitRequestBody {

	abstract fun getBody(): Map<String, Any>

	fun toRequestBody(): RequestBody {
		return JSONObject(getBody())
			.toString()
			.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
	}

}