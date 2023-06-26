package com.dcns.dailycost

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dcns.dailycost.data.datasource.local.AppDatabase
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.datasource.local.LocalExpenseDataProvider
import com.dcns.dailycost.data.datasource.local.LocalIncomeDataProvider
import com.dcns.dailycost.data.datasource.local.dao.CategoryDao
import com.dcns.dailycost.data.datasource.local.dao.ExpenseDao
import com.dcns.dailycost.data.datasource.local.dao.IncomeDao
import com.dcns.dailycost.foundation.extension.toCategoryDb
import com.dcns.dailycost.foundation.extension.toExpense
import com.dcns.dailycost.foundation.extension.toExpenseDb
import com.dcns.dailycost.foundation.extension.toIncome
import com.dcns.dailycost.foundation.extension.toIncomeDb
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseRelationTest {

    private lateinit var db: AppDatabase

    private lateinit var incomeDao: IncomeDao
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao

    @Before
    fun create_db() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        incomeDao = db.incomeDao()
        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()

        categoryDao.insertCategoryDb(LocalCategoryDataProvider.other.toCategoryDb())
    }

    @After
    @Throws(IOException::class)
    fun close_db() {
        db.close()
    }

    @Test
    fun incomeWithCategoryRelationTest() = runTest {
        val income = LocalIncomeDataProvider.income1.copy(
            category = LocalCategoryDataProvider.other
        )

        incomeDao.insertIncome(income.toIncomeDb())

        val incomeFromDb = incomeDao.getIncomeById(income.id).firstOrNull()

        assert(incomeFromDb != null) { "income from db null" }
        assert(incomeFromDb!!.toIncome() == income) { "income not equal" }
    }

    @Test
    fun expenseWithCategoryRelationTest() = runTest {
        val expense = LocalExpenseDataProvider.expense1.copy(
            category = LocalCategoryDataProvider.other
        )

        expenseDao.insertExpense(expense.toExpenseDb())

        val expenseFromDb = expenseDao.getExpenseById(expense.id).firstOrNull()

        assert(expenseFromDb != null) { "expense from db null" }
        assert(expenseFromDb!!.toExpense() == expense) { "expense not equal" }
    }

}