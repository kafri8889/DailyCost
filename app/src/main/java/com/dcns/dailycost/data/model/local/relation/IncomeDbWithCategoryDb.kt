package com.dcns.dailycost.data.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dcns.dailycost.data.model.local.CategoryDb
import com.dcns.dailycost.data.model.local.IncomeDb

data class IncomeDbWithCategoryDb(
    @Embedded val incomeDb: IncomeDb,

    @Relation(
        entity = CategoryDb::class,
        parentColumn = "categoryId_income",
        entityColumn = "category_id"
    )
    val categoryDb: CategoryDb
)
