package com.dcns.dailycost

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dcns.dailycost.data.datasource.local.AppDatabase
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.datasource.local.dao.CategoryDao
import com.dcns.dailycost.data.datasource.local.dao.ExpenseDao
import com.dcns.dailycost.data.datasource.local.dao.NoteDao
import com.dcns.dailycost.data.datasource.remote.handlers.NoteHandler
import com.dcns.dailycost.data.model.remote.response.AddNoteResponse
import com.dcns.dailycost.data.model.remote.response.DeleteResponse
import com.dcns.dailycost.data.model.remote.response.EditNoteResponse
import com.dcns.dailycost.data.model.remote.response.GetNoteResponse
import com.dcns.dailycost.data.repository.CategoryRepository
import com.dcns.dailycost.data.repository.NoteRepository
import com.dcns.dailycost.domain.repository.ICategoryRepository
import com.dcns.dailycost.domain.repository.IExpenseRepository
import com.dcns.dailycost.domain.repository.INoteRepository
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.use_case.category.GetLocalCategoryUseCase
import com.dcns.dailycost.domain.use_case.category.InputLocalCategoryUseCase
import com.dcns.dailycost.domain.util.GetCategoryBy
import com.dcns.dailycost.domain.util.InputActionType
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class AppDbTest {

    private lateinit var db: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var expenseDao: ExpenseDao
    private lateinit var categoryDao: CategoryDao

    private lateinit var noteRepository: INoteRepository
    private lateinit var expenseRepository: IExpenseRepository
    private lateinit var categoryRepository: ICategoryRepository

    private val noteHandler = object : NoteHandler {
        override suspend fun addNote(
            token: String,
            title: RequestBody,
            body: RequestBody,
            date: RequestBody,
            userId: RequestBody,
            file: MultipartBody.Part
        ): Response<AddNoteResponse> {
            return Response.success(null)
        }

        override suspend fun editNote(
            token: String,
            body: RequestBody
        ): Response<EditNoteResponse> {
            return Response.success(null)
        }

        override suspend fun deleteNote(
            token: String,
            body: RequestBody
        ): Response<DeleteResponse> {
            return Response.success(null)
        }

        override suspend fun getNoteById(userId: Int, token: String): Response<GetNoteResponse> {
            return Response.success(null)
        }

    }

//    private val expenseHandler: ExpenseHandler

    private lateinit var categoryUseCases: CategoryUseCases

    @Before
    fun create_db() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        noteDao = db.noteDao()
        expenseDao = db.expenseDao()
        categoryDao = db.categoryDao()

        noteRepository = NoteRepository(noteHandler, noteDao)
//        expenseRepository = ExpenseRepository(expenseHandler, expenseDao)
        categoryRepository = CategoryRepository(categoryDao)

        categoryUseCases = CategoryUseCases(
            getLocalCategoryUseCase = GetLocalCategoryUseCase(categoryRepository),
            inputLocalCategoryUseCase = InputLocalCategoryUseCase(categoryRepository)
        )
    }

    @After
    @Throws(IOException::class)
    fun close_db() {
        db.close()
    }

    @Test
    fun `input and get all local category`() = runTest {
        val categories = arrayOf(
            LocalCategoryDataProvider.Expense.bill,
            LocalCategoryDataProvider.Expense.entertainment
        )

        categoryUseCases.inputLocalCategoryUseCase(
            inputActionType = InputActionType.Insert,
        )

        val categoriesFromGetUseCase = categoryUseCases.getLocalCategoryUseCase()
            .firstOrNull()

        assert(categoriesFromGetUseCase != null) { "null categories" }

        assert(categoriesFromGetUseCase!!.all { it in categories }) {
            "category output is not equals as the input category"
        }
    }

    @Test
    fun `input and get local category by id`() = runTest {
        val categories = arrayOf(
            LocalCategoryDataProvider.Expense.bill,
            LocalCategoryDataProvider.Expense.entertainment
        )

        categoryUseCases.inputLocalCategoryUseCase(
            inputActionType = InputActionType.Insert,
            *categories
        )

        val categoriesFromGetUseCase = categoryUseCases.getLocalCategoryUseCase(
            getCategoryBy = GetCategoryBy.ID(LocalCategoryDataProvider.Expense.bill.id)
        ).firstOrNull()

        assert(categoriesFromGetUseCase != null) { "null categories" }

        assert(categoriesFromGetUseCase!!.getOrNull(0) != null) {
            "cannot find category"
        }
    }

}