package com.dcns.dailycost.foundation.common

import okhttp3.RequestBody

abstract class RetrofitRequestBody: Jsonable() {

    abstract fun toRequestBody(): RequestBody

}