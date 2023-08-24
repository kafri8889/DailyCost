package com.dcns.dailycost.domain.use_case

import com.dcns.dailycost.domain.use_case.category.GetLocalCategoryUseCase
import com.dcns.dailycost.domain.use_case.category.InputLocalCategoryUseCase

data class CategoryUseCases(
	val getLocalCategoryUseCase: GetLocalCategoryUseCase,
	val inputLocalCategoryUseCase: InputLocalCategoryUseCase
)
