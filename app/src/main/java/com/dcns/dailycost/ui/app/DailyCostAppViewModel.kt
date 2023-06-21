package com.dcns.dailycost.ui.app

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.BalanceRepository
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyCostAppViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val userBalanceRepository: BalanceRepository
): BaseViewModel<DailyCostAppState, DailyCostAppAction, DailyCostAppUiEvent>() {

//    private val internetObserver = Observer<Boolean> { have ->
//        Timber.i("have internet: $have")
//
//        if (have) {
//            val mState = state.value
//
//            val userIsLoggedIn = mState.userCredential != null && mState.userCredential.isLoggedIn
//            // TODO: Psot ke api kalo datanya berubah aja (bikin datastore baru)
//            val anyDataHasChanged = true
//
//            // if user has logged in, and there is data that has changed (from offline mode)
//            // post local data to server
//            if (userIsLoggedIn && anyDataHasChanged) {
//                viewModelScope.launch {
//                    launch {
//                        try {
//                            // TODO: Jangan topup, pake yg put
//                            depoUseCases.topUpDepoUseCase(
//                                token = mState.userCredential!!.getAuthToken(),
//                                body = DepoRequestBody(
//                                    id = mState.userCredential.id.toInt(),
//                                    cash = mState.userBalance.cash.toInt(),
//                                    eWallet = mState.userBalance.eWallet.toInt(),
//                                    bankAccount = mState.userBalance.bankAccount.toInt()
//                                ).toRequestBody()
//                            ).let { response ->
//                                if (response.isSuccessful) {
//                                    Timber.i("Update balance successfully")
//                                } else {
//                                    val errorResponse = Gson().fromJson(
//                                        response.errorBody()?.charStream(),
//                                        ErrorResponse::class.java
//                                    )
//
//                                    Timber.e(errorResponse.message)
//                                }
//                            }
//                        } catch (e: Exception) { Timber.e(e) }
//                    }
//
//                    // TODO: Post note, dll
//                }
//            }
//        }
//    }

    init {
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
}