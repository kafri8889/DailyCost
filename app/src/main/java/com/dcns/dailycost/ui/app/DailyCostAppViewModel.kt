package com.dcns.dailycost.ui.app

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.data.repository.BalanceRepository
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyCostAppViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val userBalanceRepository: BalanceRepository,
    private val connectivityManager: ConnectivityManager,
    private val depoUseCases: DepoUseCases
): BaseViewModel<DailyCostAppState, DailyCostAppAction, DailyCostAppUiEvent>() {

    private val internetObserver = Observer<Boolean> { have ->
        Timber.i("have internet: $have")

        if (have) {
            val mState = state.value

            mState.userCredential?.let { credential ->
                viewModelScope.launch {
                    depoUseCases.topUpDepoUseCase(
                        token = credential.getAuthToken(),
                        body = DepoRequestBody(
                            id = credential.id.toInt(),
                            cash = mState.userBalance.cash.toInt(),
                            eWallet = mState.userBalance.eWallet.toInt(),
                            bankAccount = mState.userBalance.bankAccount.toInt()
                        ).toRequestBody()
                    ).let { response ->
                        if (response.isSuccessful) {
                            Timber.i("Update balance successfully")
                        } else {
                            val errorResponse = Gson().fromJson(
                                response.errorBody()?.charStream(),
                                ErrorResponse::class.java
                            )

                            Timber.e(errorResponse.message)
                        }
                    }

                    // TODO: Post note, dll
                }
            }
        }
    }

    init {
        connectivityManager.isNetworkAvailable.observeForever(internetObserver)

        viewModelScope.launch {
            userBalanceRepository.getUserBalance.collect { balance ->
                updateState {
                    copy(
                        userBalance = balance
                    )
                }
            }
        }

        viewModelScope.launch {
            userCredentialRepository.getUserCredential.collect { cred ->
                Timber.i("credential: $cred | ${cred.isLoggedIn}")

                updateState {
                    copy(
                        userCredential = cred
                    )
                }
            }
        }
    }

    override fun defaultState(): DailyCostAppState = DailyCostAppState()

    override fun onAction(action: DailyCostAppAction) {
        when (action) {
            is DailyCostAppAction.UpdateCurrentDestinationRoute -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            currentDestinationRoute = action.route
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        connectivityManager.isNetworkAvailable.removeObserver(internetObserver)

        super.onCleared()
    }
}