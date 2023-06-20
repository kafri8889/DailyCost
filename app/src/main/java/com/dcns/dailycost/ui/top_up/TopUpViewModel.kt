package com.dcns.dailycost.ui.top_up

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.base.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    private val balanceUseCases: BalanceUseCases,
    private val depoUseCases: DepoUseCases
): BaseViewModel<TopUpState, TopUpAction, UiEvent>() {

    init {
        viewModelScope.launch {
            balanceUseCases.getLocalBalanceUseCase().collect { balance ->
                updateState {
                    copy(
                        balance = balance,
                        amount = when (selectedWalletType) {
                            WalletType.Cash -> balance.cash
                            WalletType.EWallet -> balance.eWallet
                            WalletType.BankAccount -> balance.bankAccount
                        }
                    )
                }
            }
        }
    }

    override fun defaultState(): TopUpState = TopUpState()

    override fun onAction(action: TopUpAction) {
        when (action) {
            is TopUpAction.ChangeSelectedWalletType -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            selectedWalletType = action.type
                        )
                    }
                }
            }
            is TopUpAction.ChangeWalletBalance -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            amount = action.amount
                        )
                    }
                }
            }
            is TopUpAction.TopUp -> {
                viewModelScope.launch {

                }
            }
        }
    }
}