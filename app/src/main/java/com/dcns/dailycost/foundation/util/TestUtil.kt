package com.dcns.dailycost.foundation.util

import com.dcns.dailycost.foundation.common.IResponse
import retrofit2.Response

object TestUtil {

    const val adminUserId = 73
    const val adminToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NzMsImVtYWlsIjoiYWRtaW4iLCJpYXQiOjE2ODc2NzgwNDksImV4cCI6MTY4Nzc2NDQ0OX0.dKYzZY5hKbX4SfOlXICq3chgu2fVis_ezAdmG7BR7F0"

    fun printResponse(response: Response<out IResponse>) {
        println(response.body())
        println(response.code())
        println(response.message())
        println(response.raw())
    }

}