package com.dcns.dailycost.data.model.networking.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class RegisterRequestBody(
    val name: String,
    val email: String,
    val password: String
): RetrofitRequestBody {

    override fun toRequestBody(): RequestBody {
        val body = JSONObject(
            mapOf(
                "name" to name,
                "email" to email,
                "password" to password
            )
        )

        return body
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}
