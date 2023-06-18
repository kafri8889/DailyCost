package com.dcns.dailycost.domain.util

sealed class GetNoteMethod {

    data class Api(
        val token: String
    ): GetNoteMethod()

    class Local(): GetNoteMethod()

}