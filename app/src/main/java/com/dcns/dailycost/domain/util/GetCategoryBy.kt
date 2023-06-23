package com.dcns.dailycost.domain.util

sealed class GetCategoryBy {

    data class ID(val id: Int): GetCategoryBy()

    object All: GetCategoryBy()

}