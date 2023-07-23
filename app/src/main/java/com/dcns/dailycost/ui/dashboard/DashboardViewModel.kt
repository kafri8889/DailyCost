package com.dcns.dailycost.ui.dashboard

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.data.Status
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.ExpenseUseCases
import com.dcns.dailycost.domain.use_case.IncomeUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.IResponse
import com.dcns.dailycost.foundation.common.SharedUiEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userCredentialUseCases: UserCredentialUseCases,
    private val connectivityManager: ConnectivityManager,
    private val expenseUseCases: ExpenseUseCases,
    private val incomeUseCases: IncomeUseCases,
    private val sharedUiEvent: SharedUiEvent,
    private val depoUseCases: DepoUseCases,
): BaseViewModel<DashboardState, DashboardAction>() {

    init {
        viewModelScope.launch {
            sharedUiEvent.uiEvent.filterNotNull().collect { event ->
                when (event) {
                    is DashboardUiEvent.TopUpSuccess -> sendEvent(event)
                }
            }
        }

        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.asFlow().collect { have ->
                Timber.i("have internet: $have")

                updateState {
                    copy(
                        internetConnectionAvailable = have
                    )
                }

                // Kalo ga ada koneksi internet, show snackbar
                if (!have) {
                    sendEvent(DashboardUiEvent.NoInternetConnection())
                }
            }
        }

        viewModelScope.launch {
            expenseUseCases.getLocalExpenseUseCase().collect { expenseList ->
                updateState {
                    copy(
                        expenses = expenseList
                    )
                }
            }
        }

        viewModelScope.launch {
            incomeUseCases.getLocalIncomeUseCase().collect { incomeList ->
                updateState {
                    copy(
                        incomes = incomeList
                    )
                }
            }
        }

        viewModelScope.launch {
            depoUseCases.getLocalBalanceUseCase().collect { balance ->
                updateState {
                    copy(
                        balance = balance
                    )
                }
            }
        }

        viewModelScope.launch {
            userCredentialUseCases.getUserCredentialUseCase().collect { cred ->
                updateState {
                    copy(
                        credential = cred
                    )
                }
            }
        }
    }

    private suspend fun getRemoteBalance(): Resource<IResponse> {
        val mState = state.value

        return depoUseCases.getRemoteBalanceUseCase(
            token = mState.credential.getAuthToken(),
            userId = mState.credential.id.toIntOrNull() ?: -1
        ).let { response ->
            if (response.isSuccessful) {
                val balanceResponseData = response.body()?.data

                balanceResponseData?.let {
                    depoUseCases.updateLocalBalanceUseCase(
                        cash = balanceResponseData.cash.toDouble(),
                        eWallet = balanceResponseData.eWallet.toDouble(),
                        bankAccount = balanceResponseData.bankAccount.toDouble()
                    )
                }

                Timber.i("get remote balance success")

                return Resource.success(response.body())
            }

            val errorResponse = Gson().fromJson(
                response.errorBody()?.charStream(),
                ErrorResponse::class.java
            )

            Resource.error(errorResponse.message, null)
        }
    }

    override fun defaultState(): DashboardState = DashboardState()

    override fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.Refresh -> viewModelScope.launch {
                // Kalo ga ada koneksi internet, show snackbar
                if (!state.value.internetConnectionAvailable) {
                    sendEvent(DashboardUiEvent.NoInternetConnection())
                    return@launch
                }

                updateState {
                    copy(
                        isRefreshing = true
                    )
                }

                val response = listOf(
                    getRemoteBalance()
                )

                response.filter { it.status == Status.Error }.forEach {
                    sendEvent(DashboardUiEvent.GetRemoteFailed(it.message ?: ""))
                }

                updateState {
                    copy(
                        isRefreshing = false
                    )
                }
            }
        }
    }
}