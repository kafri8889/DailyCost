package com.dcns.dailycost.domain.use_case.category

import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.domain.repository.ICategoryRepository
import com.dcns.dailycost.domain.util.GetCategoryBy
import com.dcns.dailycost.foundation.extension.toCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Use case untuk mendapatkan kategori dari database lokal
 */
class GetLocalCategoryUseCase(
    private val categoryRepository: ICategoryRepository
) {

    operator fun invoke(
        getCategoryBy: GetCategoryBy = GetCategoryBy.All
    ): Flow<List<Category>> {
        return when (getCategoryBy) {
            is GetCategoryBy.Name -> {
                categoryRepository.getCategoryByName(getCategoryBy.name)
                    .filterNotNull() // Filter value yg tidak null
                    .map { listOf(it.toCategory()) }
            }
            is GetCategoryBy.ID -> {
                categoryRepository.getCategoryByID(getCategoryBy.id)
                    .filterNotNull() // Filter value yg tidak null
                    .map { listOf(it.toCategory()) }
            }
            GetCategoryBy.All -> categoryRepository.getAllCategory()
                .filterNotNull() // Filter value yg tidak null
                .map { it.map { it.toCategory() } }
        }
    }

}