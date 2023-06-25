package com.dcns.dailycost.foundation.util

import com.dcns.dailycost.data.model.remote.response.DepoResponse
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.data.model.remote.response.NoteResponse
import com.dcns.dailycost.data.model.remote.response.RegisterResponse
import com.dcns.dailycost.data.model.remote.response.ShoppingResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import retrofit2.Response

object TestUtil {

    const val adminUserId = 73
    const val adminToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NzMsImVtYWlsIjoiYWRtaW4iLCJpYXQiOjE2ODc2NzgwNDksImV4cCI6MTY4Nzc2NDQ0OX0.dKYzZY5hKbX4SfOlXICq3chgu2fVis_ezAdmG7BR7F0"

    fun <T> printResponse(response: Response<T>) {
        val body = response.body()

        println(response.code())
        println(response.message())
        println(response.raw())

        when (body) {
            is UploadImageResponse -> println(body.toString())
            is IncomePostResponse -> println(body.toString())
            is IncomeGetResponse -> println(body.toString())
            is ShoppingResponse -> println(body.toString())
            is RegisterResponse -> println(body.toString())
            is ErrorResponse -> println(body.toString())
            is LoginResponse -> println(body.toString())
            is DepoResponse -> println(body.toString())
            is NoteResponse -> println(body.toString())
        }
    }

}