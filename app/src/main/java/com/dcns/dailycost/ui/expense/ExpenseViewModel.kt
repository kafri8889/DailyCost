package com.dcns.dailycost.ui.expense

import androidx.lifecycle.SavedStateHandle
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseUseCases: ExpenseUseCases,
    savedStateHandle: SavedStateHandle
): BaseViewModel<ExpenseState, ExpenseAction>() {

    override fun defaultState(): ExpenseState = ExpenseState()

    override fun onAction(action: ExpenseAction) {

    }
}