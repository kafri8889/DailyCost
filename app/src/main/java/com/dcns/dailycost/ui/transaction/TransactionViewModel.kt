package com.dcns.dailycost.ui.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.datasource.local.LocalCategoryDataProvider
import com.dcns.dailycost.data.model.remote.request_body.AddExpenseRequestBody
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.worker.Workers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val userCredentialUseCases: UserCredentialUseCases,
    private val expenseUseCases: ExpenseUseCases,
    private val incomeUseCases: IncomeUseCases,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
): BaseViewModel<TransactionState, TransactionAction>() {

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

    }
}