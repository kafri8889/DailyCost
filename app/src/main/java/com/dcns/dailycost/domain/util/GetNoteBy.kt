package com.dcns.dailycost.domain.util

sealed class GetNoteBy {

    data class UserID(val userId: Int): GetNoteBy()

    data class ID(val id: Int): GetNoteBy()

    object All: GetNoteBy()

}