package com.dcns.dailycost.foundation.common

import okhttp3.RequestBody

interface RetrofitRequestBody {

    fun toRequestBody(): RequestBody

}