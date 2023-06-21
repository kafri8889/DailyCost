package com.dcns.dailycost.data.model.remote.request_body.note

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class NewNoteRequestBody(
    val userId: Int,
    val title: String,
    val file: String,
    val body: String,
    val date: String
): RetrofitRequestBody() {

    override fun toRequestBody(): RequestBody {
        val body = JSONObject(
            mapOf(
                "title" to title,
                "body" to body,
                "file" to file,
                "date" to date,
                "user_id" to userId,
            )
        )

        return body
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}

