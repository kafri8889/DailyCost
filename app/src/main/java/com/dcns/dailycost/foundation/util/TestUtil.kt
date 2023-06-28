package com.dcns.dailycost.foundation.util

import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.DepoResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import com.dcns.dailycost.data.model.remote.response.IncomeGetResponse
import com.dcns.dailycost.data.model.remote.response.IncomePostResponse
import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.data.model.remote.response.RegisterResponse
import com.dcns.dailycost.data.model.remote.response.UploadImageResponse
import com.dcns.dailycost.data.model.remote.response.expense.AddExpenseResponse
import com.dcns.dailycost.data.model.remote.response.expense.GetExpenseResponse
import retrofit2.Response

object TestUtil {

    const val adminUserId = 73
    const val adminToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NzMsImVtYWlsIjoiYWRtaW4iLCJpYXQiOjE2ODc5NDc4MjUsImV4cCI6MTY4ODAzNDIyNX0.fVU8FCEwFLmTYonK43sixk5C21bpZdAJYDWTwNfJRv8"

    fun <T> printResponse(response: Response<T>) {
        println(getPrintedResponse(response))
    }

    fun <T> getPrintedResponse(response: Response<T>): String {
        val bldr = StringBuilder()
        val body = response.body()

        bldr.append("${response.code()} \n")
        bldr.append("${response.message()} \n")
        bldr.append("${response.raw()} \n")

        when (body) {
            is UploadImageResponse -> bldr.append("$body \n")
            is IncomePostResponse -> bldr.append("$body \n")
            is IncomeGetResponse -> bldr.append("$body \n")
            is EditNoteResponse -> bldr.append("$body \n")
            is AddExpenseResponse -> bldr.append("$body \n")
            is RegisterResponse -> bldr.append("$body \n")
            is AddNoteResponse -> bldr.append("$body \n")
            is GetExpenseResponse -> bldr.append("$body \n")
            is DeleteResponse -> bldr.append("$body \n")
            is ErrorResponse -> bldr.append("$body \n")
            is LoginResponse -> bldr.append("$body \n")
            is DepoResponse -> bldr.append("$body \n")
            is GetNoteResponse -> bldr.append("$body \n")
        }

        return bldr.toString()
    }

}