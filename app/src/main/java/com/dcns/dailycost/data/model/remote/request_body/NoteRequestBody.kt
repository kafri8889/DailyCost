package com.dcns.dailycost.data.model.remote.request_body

import com.dcns.dailycost.foundation.common.RetrofitRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class NoteRequestBody(
    val title: String = "judul",
    val body: String = "contoh deskripsi",
    val date: Long = 0,
    val userId: Int = 1
): RetrofitRequestBody {

    override fun toRequestBody(): RequestBody {
        val body = JSONObject(
            mapOf(
                "title" to title,
                "body" to body,
                "date" to date,
                "user_id" to userId,
            )
        )

        return body
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}

