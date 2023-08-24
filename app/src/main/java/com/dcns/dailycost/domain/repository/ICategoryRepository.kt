package com.dcns.dailycost.domain.repository

import com.dcns.dailycost.data.model.local.CategoryDb
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {

	fun getAllCategory(): Flow<List<CategoryDb>>

	fun getCategoryByID(mID: Int): Flow<CategoryDb?>

	fun getCategoryByName(name: String): Flow<CategoryDb?>

	suspend fun deleteCategoryDb(vararg categoryDb: CategoryDb)

	suspend fun updateCategoryDb(vararg categoryDb: CategoryDb)

	suspend fun upsertCategoryDb(vararg categoryDb: CategoryDb)

	suspend fun insertCategoryDb(vararg categoryDb: CategoryDb)

}