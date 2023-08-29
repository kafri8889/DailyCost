package com.dcns.dailycost.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dcns.dailycost.data.model.local.CategoryDb
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

	@Query("SELECT * FROM category_table")
	fun getAllCategory(): Flow<List<CategoryDb>>

	@Query("SELECT * FROM category_table WHERE category_id LIKE :mID")
	fun getCategoryByID(mID: Int): Flow<CategoryDb?>

	@Query("SELECT * FROM category_table WHERE category_name LIKE :name")
	fun getCategoryByName(name: String): Flow<CategoryDb?>

	@Delete
	fun deleteCategoryDb(vararg categoryDb: CategoryDb)

	@Update
	fun updateCategoryDb(vararg categoryDb: CategoryDb)

	@Upsert
	fun upsertCategoryDb(vararg categoryDb: CategoryDb)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertCategoryDb(vararg categoryDb: CategoryDb)

}