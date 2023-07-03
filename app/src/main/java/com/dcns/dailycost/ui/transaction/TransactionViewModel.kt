package com.dcns.dailycost.ui.transaction

import androidx.lifecycle.SavedStateHandle
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val expenseUseCases: ExpenseUseCases,
    savedStateHandle: SavedStateHandle
): BaseViewModel<TransactionState, TransactionAction>() {

    override fun defaultState(): TransactionState = TransactionState()

    override fun onAction(action: TransactionAction) {

    }
}