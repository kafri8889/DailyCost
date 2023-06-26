package com.dcns.dailycost.foundation.common

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

abstract class RetrofitRequestBody: Jsonable() {

    abstract fun getBody(): Map<String, Any>

    fun appendToMultipartBody(builder: MultipartBody.Builder) {
        getBody().forEach { (k, v) ->
            builder.addFormDataPart(k, v.toString())
        }
    }

    fun toRequestBody(): RequestBody {
        return JSONObject(getBody())
            .toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

}