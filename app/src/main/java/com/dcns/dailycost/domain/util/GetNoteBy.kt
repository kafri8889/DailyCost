package com.dcns.dailycost.domain.util

sealed class GetNoteBy {

    data class ID(val userId: Int): GetNoteBy()

    object All: GetNoteBy()

}