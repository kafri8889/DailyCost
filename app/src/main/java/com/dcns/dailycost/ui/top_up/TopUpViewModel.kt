package com.dcns.dailycost.ui.top_up

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.base.UiEvent
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.SharedUiEvent
import com.dcns.dailycost.foundation.common.Workers
import com.dcns.dailycost.foundation.extension.enqueue
import com.dcns.dailycost.ui.dashboard.DashboardUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    private val userCredentialRepository: IUserCredentialRepository,
    private val connectivityManager: ConnectivityManager,
    private val balanceUseCases: BalanceUseCases,
    private val sharedUiEvent: SharedUiEvent
): BaseViewModel<TopUpState, TopUpAction, UiEvent>() {

    init {
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.asFlow().collect { have ->
                Timber.i("have internet: $have")

                updateState {
                    copy(
                        internetConnectionAvailable = have
                    )
                }
            }
        }

        viewModelScope.launch {
            userCredentialRepository.getUserCredential.collect { cred ->
                updateState {
                    copy(
                        credential = cred
                    )
                }
            }
        }
        viewModelScope.launch {
            balanceUseCases.getLocalBalanceUseCase().collect { balance ->
                updateState {
                    copy(
                        balance = balance
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
                    val mState = state.value

                    val cash = if (mState.selectedWalletType == WalletType.Cash) {
                        mState.amount
                    } else 0.0

                    val eWallet = if (mState.selectedWalletType == WalletType.EWallet) {
                        mState.amount
                    } else 0.0

                    val bankAccount = if (mState.selectedWalletType == WalletType.BankAccount) {
                        mState.amount
                    } else 0.0

                    Workers.topUpBalanceWorker(
                        DepoRequestBody(
                            id = mState.credential.id.toInt(),
                            cash = cash.toInt(),
                            eWallet = eWallet.toInt(),
                            bankAccount = bankAccount.toInt()
                        )
                    ).enqueue(action.context)

                    sharedUiEvent.sendUiEvent(DashboardUiEvent.TopUpSuccess())
                }
            }
        }
    }
}