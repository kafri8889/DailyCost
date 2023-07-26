package com.dcns.dailycost.foundation.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dcns.dailycost.data.CategoryIcon
import com.dcns.dailycost.data.model.Category
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.util.GetNoteBy
import com.dcns.dailycost.domain.util.InputActionType
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.extension.toExpense
import com.dcns.dailycost.foundation.extension.toIncome
import com.dcns.dailycost.foundation.extension.toNote
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

/**
 * Worker untuk mengsinkronisasikan semua data dari remote ke lokal
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val noteUseCases: NoteUseCases,
    private val depoUseCases: DepoUseCases,
    private val incomeUseCases: IncomeUseCases,
    private val expenseUseCases: ExpenseUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val userCredentialUseCases: UserCredentialUseCases
): CoroutineWorker(context, params) {

    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        val credential = userCredentialUseCases.getUserCredentialUseCase().firstOrNull()
            ?: return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to "Null credential"
                )
            )

        val results = listOf(
            getExpense(credential),
            getBalance(credential),
            getIncome(credential),
//            getNote(credential)
        )

        return if (results.all { it is Result.Success }) Result.success()
        else Result.failure(workDataOf(
            Workers.ARG_WORKER_MESSAGE_KEY to "Something went wrong!"
        ))
    }

    private suspend fun getExpense(credential: UserCredential): Result {
        // Get remote expense
        expenseUseCases.getRemoteExpenseUseCase(
            token = credential.getAuthToken(),
            userId = credential.id.toInt()
        ).let { response ->
            if (response.isSuccessful) {
                val expenseResponse = response.body()

                expenseResponse?.let {
                    val categories = categoryUseCases.getLocalCategoryUseCase().firstOrNull() ?: emptyList()
                    Timber.i("sync expenses to db...")
                    withContext(Dispatchers.IO) {

                        // Convet [GetExpenseResponseData] to [Expense]
                        val results = expenseResponse
                            .data
                            .results

                        val mappedResults = results.map {
                            it.toExpense(
                                userId = credential.id.toInt(),
                                date = { date ->
                                    Timber.i("try to parse date: $date")
                                    CommonDateFormatter.tryParseApi(date) ?: 0
                                },
                                category = { categoryName ->
                                    // Get local category
                                    var category = categories.find { it.name == categoryName }

                                    // If category not null, use
                                    if (category != null) category
                                    else {
                                        // If null, create new category and insert to db
                                        category = Category(
                                            id = Random(System.currentTimeMillis()).nextInt(),
                                            name = categoryName,
                                            icon = CategoryIcon.Other
                                        )

                                        categoryUseCases.inputLocalCategoryUseCase(
                                            inputActionType = InputActionType.Upsert,
                                            category
                                        )

                                        category
                                    }
                                }
                            )
                        }

                        // Sync local expense with remote expense
                        expenseUseCases.syncLocalWithRemoteExpenseUseCase(mappedResults)
                        Timber.i("sync expenses to db success")
                    }
                }

                Timber.i("get remote expense success")

                return Result.success()
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            Timber.e("failed to get remote expense: ${errorResponse.message}")

            return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                )
            )
        }
    }

    private suspend fun getIncome(credential: UserCredential): Result {
        // Get remote income
        incomeUseCases.getRemoteIncomeUseCase(
            token = credential.getAuthToken(),
            userId = credential.id.toInt()
        ).let { response ->
            if (response.isSuccessful) {
                val incomeResponse = response.body()

                incomeResponse?.let {
                    val categories = categoryUseCases.getLocalCategoryUseCase().firstOrNull() ?: emptyList()
                    Timber.i("sync income to db...")
                    withContext(Dispatchers.IO) {

                        // Convet [IncomeResponseData] to [Income]
                        val results = incomeResponse
                            .data

                        val mappedResults = results.map {
                            it.toIncome(
                                date = { date ->
                                    CommonDateFormatter.tryParseApi(date) ?: 0
                                },
                                category = { categoryName ->
                                    // Get local category
                                    var category = categories.find { it.name == categoryName }

                                    // If category not null, use
                                    if (category != null) category
                                    else {
                                        // If null, create new category and insert to db
                                        category = Category(
                                            id = Random(System.currentTimeMillis()).nextInt(),
                                            name = categoryName,
                                            icon = CategoryIcon.Other
                                        )

                                        categoryUseCases.inputLocalCategoryUseCase(
                                            inputActionType = InputActionType.Upsert,
                                            category
                                        )

                                        category
                                    }
                                }
                            )
                        }

                        // Sync local income with remote expense
                        incomeUseCases.syncLocalWithRemoteIncomeUseCase(mappedResults)
                        Timber.i("sync income to db success")
                    }
                }

                Timber.i("get remote income success")

                return Result.success()
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            Timber.e("failed to get remote expense: ${errorResponse.message}")

            return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                )
            )
        }
    }

    private suspend fun getNote(credential: UserCredential): Result {
        noteUseCases.getRemoteNoteUseCase(
            token = credential.getAuthToken(),
            getNoteBy = GetNoteBy.UserID(credential.id.toInt())
        ).let { response ->
            if (response.isSuccessful) {
                val noteResponse = response.body()

                noteResponse?.let {
                    Timber.i("sync notes to db...")
                    withContext(Dispatchers.IO) {
                        noteUseCases.syncLocalWithRemoteNoteUseCase(
                            noteResponse.data
                                .map { it.toNote() }
                        )
                    }
                }

                Timber.i("get remote note success")

                return Result.success()
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            Timber.e("failed to get remote note: ${errorResponse.message}")

            return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                )
            )
        }
    }

    private suspend fun getBalance(credential: UserCredential): Result {
        depoUseCases.getRemoteBalanceUseCase(
            token = credential.getAuthToken(),
            userId = credential.id.toInt()
        ).let { response ->
            if (response.isSuccessful) {
                val balanceResponseData = response.body()?.data

                balanceResponseData?.let {
                    depoUseCases.updateLocalBalanceUseCase(
                        cash = balanceResponseData.cash.toDouble(),
                        eWallet = balanceResponseData.eWallet.toDouble(),
                        bankAccount = balanceResponseData.bankAccount.toDouble()
                    )
                }

                Timber.i("get remote balance success")

                return Result.success()
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            // User ga punya saldo
            if (response.code() == 404) {
                depoUseCases.updateLocalBalanceUseCase(
                    cash = 0.0,
                    eWallet = 0.0,
                    bankAccount = 0.0
                )
            }

            Timber.e("failed to get remote balance: ${errorResponse.message}")

            return Result.failure(
                workDataOf(
                    Workers.ARG_WORKER_MESSAGE_KEY to errorResponse.message
                )
            )
        }
    }

}