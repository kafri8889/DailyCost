package com.dcns.dailycost.ui.top_up

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.response.DepoResponse
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    private val userCredentialRepository: IUserCredentialRepository,
    private val connectivityManager: ConnectivityManager,
    private val balanceUseCases: BalanceUseCases,
    private val depoUseCases: DepoUseCases
): BaseViewModel<TopUpState, TopUpAction, TopUpUiEvent>() {

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

    private suspend fun updateLocalBalance(
        cash: Double,
        eWallet: Double,
        bankAccount: Double
    ) {
        balanceUseCases.updateLocalBalanceUseCase(
            cash = cash,
            eWallet = eWallet,
            bankAccount = bankAccount
        )
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

                    updateState {
                        copy(
                            isLoading = true
                        )
                    }

                    val cash = if (mState.selectedWalletType == WalletType.Cash) {
                        mState.amount
                    } else mState.balance.cash

                    val eWallet = if (mState.selectedWalletType == WalletType.EWallet) {
                        mState.amount
                    } else mState.balance.eWallet

                    val bankAccount = if (mState.selectedWalletType == WalletType.BankAccount) {
                        mState.amount
                    } else mState.balance.bankAccount

                    if (mState.internetConnectionAvailable) {
                        depoUseCases.topUpDepoUseCase(
                            token = mState.credential.getAuthToken(),
                            body = DepoRequestBody(
                                id = mState.credential.id.toInt(),
                                cash = cash.toInt(),
                                eWallet = eWallet.toInt(),
                                bankAccount = bankAccount.toInt()
                            ).toRequestBody()
                        ).let { response ->
                            if (response.isSuccessful) {
                                val body = response.body() as DepoResponse

                                updateLocalBalance(
                                    cash = body.data.cash.toDouble(),
                                    eWallet = body.data.eWallet.toDouble(),
                                    bankAccount = body.data.bankAccount.toDouble()
                                )

                                sendEvent(TopUpUiEvent.TopUpSuccess)

                                updateState {
                                    copy(
                                        isLoading = false
                                    )
                                }

                                return@launch
                            }

                            val errorResponse = Gson().fromJson(
                                response.errorBody()?.charStream(),
                                ErrorResponse::class.java
                            )

                            sendEvent(TopUpUiEvent.TopUpFailed(errorResponse.message))

                            updateState {
                                copy(
                                    isLoading = false
                                )
                            }
                        }

                        return@launch
                    }

                    updateLocalBalance(
                        cash = cash,
                        eWallet = eWallet,
                        bankAccount = bankAccount
                    )

                    sendEvent(TopUpUiEvent.TopUpSuccess)

                    updateState {
                        copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}