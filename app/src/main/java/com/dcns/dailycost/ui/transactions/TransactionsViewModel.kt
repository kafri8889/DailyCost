package com.dcns.dailycost.ui.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
	private val expenseUseCases: ExpenseUseCases,
	private val incomeUseCases: IncomeUseCases,
	savedStateHandle: SavedStateHandle
): BaseViewModel<TransactionsState, TransactionsAction>(savedStateHandle, TransactionsState()) {

	private val deliveredTransactionType = savedStateHandle.getStateFlow<TransactionType?>(
		DestinationArgument.TRANSACTION_TYPE,
		null
	)

	init {
		viewModelScope.launch {
			combine(
				expenseUseCases.getLocalExpenseUseCase(),
				incomeUseCases.getLocalIncomeUseCase(),
				deliveredTransactionType
			) { expenses, incomes, transactionType ->
				Triple(expenses, incomes, transactionType)
			}.collect { (expenses, incomes, transactionType) ->
				Timber.i("Transaction type: $transactionType")
				updateState {
					copy(
						selectedTransactionType = transactionType,
						transactions = when (transactionType) {
							TransactionType.Income -> incomes
							TransactionType.Expense -> expenses
							null -> incomes + expenses
						}
					)
				}
			}
		}
	}

	override fun onAction(action: TransactionsAction) {

	}
}