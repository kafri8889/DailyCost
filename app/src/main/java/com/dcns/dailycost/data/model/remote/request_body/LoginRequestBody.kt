package com.dcns.dailycost.data.model.remote.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody

data class LoginRequestBody(
	val email: String,
	val password: String
): RetrofitRequestBody() {

	override fun getBody(): Map<String, Any> {
		return mapOf(
			"email" to email,
			"password" to password
		)
	}
}
