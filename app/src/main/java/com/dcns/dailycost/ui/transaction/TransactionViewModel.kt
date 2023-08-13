package com.dcns.dailycost.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.TransactionMode
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.remote.request_body.AddExpenseRequestBody
import com.dcns.dailycost.domain.use_case.CategoryUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.util.GetTransactionBy
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.worker.Workers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val userCredentialUseCases: UserCredentialUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val expenseUseCases: ExpenseUseCases,
    private val incomeUseCases: IncomeUseCases,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
): BaseViewModel<TransactionState, TransactionAction>() {

    private val deliveredTransactionId = savedStateHandle.getStateFlow<Int?>(DestinationArgument.TRANSACTION_ID, null)
    private val deliveredTransactionMode = savedStateHandle.getStateFlow<TransactionMode?>(DestinationArgument.TRANSACTION_MODE, null)
    private val deliveredTransactionType = savedStateHandle.getStateFlow<TransactionType?>(DestinationArgument.TRANSACTION_TYPE, null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            deliveredTransactionId
                .filterNotNull()
                .combine(deliveredTransactionType.filterNotNull()) { id, type ->
                    val li =  when (type) {
                        TransactionType.Income -> incomeUseCases.getLocalIncomeUseCase(GetTransactionBy.ID(id)).firstOrNull()
                        TransactionType.Expense -> expenseUseCases.getLocalExpenseUseCase(GetTransactionBy.ID(id)).firstOrNull()
                    }

                    if (!li.isNullOrEmpty()) li[0] to type else null to type
                }.filterNotNull().collect { (transaction, type) ->
                    Timber.i("Received transaction: $transaction")
                    updateState {
                        copy(
                            name = transaction?.name ?: name,
                            amount = transaction?.amount ?: amount,
                            payment = transaction?.payment ?: payment,
                            date = transaction?.date ?: date,
                            category = transaction?.category ?: category,
                            transactionType = type
                        )
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            deliveredTransactionMode
                .filterNotNull()
                .collect { mode ->
                    updateState {
                        copy(
                            transactionMode = mode
                        )
                    }
                }
        }

        viewModelScope.launch(Dispatchers.IO) {
            categoryUseCases.getLocalCategoryUseCase().collect { categories ->
                updateState {
                    copy(
                        availableCategory = categories
                    )
                }
            }
        }
    }

    /**
     * For testing purpose
     */
    fun performInsertTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            userCredentialUseCases.getUserCredentialUseCase().firstOrNull()?.let { credential ->
                // Chain works: Post -> Sync
                workManager.beginWith(
                    Workers.postExpenseWorker(
                        AddExpenseRequestBody(
                            amount = 1000,
                            name = "performInsertTransaction",
                            payment = WalletType.EWallet.apiName,
                            category = LocalCategoryDataProvider.other.name,
                            date = "2023-07-26",
                            userId = credential.id.toInt()
                        )
                    )
                ).then(Workers.syncWorker()).enqueue()
            }
        }
    }

    override fun defaultState(): TransactionState = TransactionState()

    override fun onAction(action: TransactionAction) {
        when (action) {
            is TransactionAction.SetTransactionType -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            transactionType = action.type
                        )
                    }
                }
            }
            is TransactionAction.SetAmount -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            amount = action.amount
                        )
                    }
                }
            }
            is TransactionAction.SetCategory -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            category = action.category
                        )
                    }
                }
            }
            is TransactionAction.SetDate -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            date = action.date
                        )
                    }
                }
            }
            is TransactionAction.SetPayment -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            payment = action.payment
                        )
                    }
                }
            }
            is TransactionAction.SetName -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            name = action.name
                        )
                    }
                }
            }
            TransactionAction.Delete -> {
                viewModelScope.launch(Dispatchers.IO) {

                }
            }
            TransactionAction.Save -> {
                viewModelScope.launch(Dispatchers.IO) {

                }
            }
        }
    }
}