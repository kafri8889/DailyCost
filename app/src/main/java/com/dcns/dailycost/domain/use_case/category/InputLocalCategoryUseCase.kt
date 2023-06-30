package com.dcns.dailycost.domain.use_case.category

import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.domain.repository.ICategoryRepository
import com.dcns.dailycost.domain.util.InputActionType
import com.dcns.dailycost.foundation.extension.toCategoryDb

/**
 * Create, Upsert, Update and Delete kategori ke database lokal
 */
class InputLocalCategoryUseCase(
    private val categoryRepository: ICategoryRepository
) {

    suspend operator fun invoke(
        inputActionType: InputActionType,
        vararg category: Category
    ) {
        val categoryDb = category
            .map { it.toCategoryDb() }
            .toTypedArray()

        when (inputActionType) {
            InputActionType.Insert -> categoryRepository.insertCategoryDb(*categoryDb)
            InputActionType.Upsert -> categoryRepository.upsertCategoryDb(*categoryDb)
            InputActionType.Update -> categoryRepository.updateCategoryDb(*categoryDb)
            InputActionType.Delete -> categoryRepository.deleteCategoryDb(*categoryDb)
        }
    }

}