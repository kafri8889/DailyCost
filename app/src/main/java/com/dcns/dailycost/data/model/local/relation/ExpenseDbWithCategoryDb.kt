package com.dcns.dailycost.data.model.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.dcns.dailycost.data.model.local.CategoryDb
import com.dcns.dailycost.data.model.local.ExpenseDb

data class ExpenseDbWithCategoryDb(
    @Embedded val expenseDb: ExpenseDb,

    @Relation(
        entity = CategoryDb::class,
        parentColumn = "categoryId_expense",
        entityColumn = "category_id"
    )
    val categoryDb: CategoryDb
)
