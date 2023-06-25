package com.dcns.dailycost.foundation.util

import com.dcns.dailycost.foundation.common.IResponse
import retrofit2.Response

object TestUtil {

    fun printResponse(response: Response<out IResponse>) {
        println(response.body())
        println(response.code())
        println(response.message())
        println(response.raw())
    }

}