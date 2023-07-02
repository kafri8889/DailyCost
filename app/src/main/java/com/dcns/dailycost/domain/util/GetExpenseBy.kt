package com.dcns.dailycost.domain.util

sealed class GetExpenseBy {

    data class ID(val id: Int): GetExpenseBy()

    object All: GetExpenseBy()

}