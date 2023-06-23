package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.local.dao.CategoryDao
import com.dcns.dailycost.data.model.local.CategoryDb
import com.dcns.dailycost.domain.repository.ICategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
): ICategoryRepository {

    override fun getAllCategory(): Flow<List<CategoryDb>> {
        return categoryDao.getAllCategory()
    }

    override fun getCategoryByID(mID: Int): Flow<CategoryDb?> {
        return categoryDao.getCategoryByID(mID)
    }

    override suspend fun deleteCategoryDb(vararg categoryDb: CategoryDb) {
        categoryDao.deleteCategoryDb(*categoryDb)
    }

    override suspend fun updateCategoryDb(vararg categoryDb: CategoryDb) {
        categoryDao.updateCategoryDb(*categoryDb)
    }

    override suspend fun upsertCategoryDb(vararg categoryDb: CategoryDb) {
        categoryDao.upsertCategoryDb(*categoryDb)
    }

    override suspend fun insertCategoryDb(vararg categoryDb: CategoryDb) {
        categoryDao.insertCategoryDb(*categoryDb)
    }
}