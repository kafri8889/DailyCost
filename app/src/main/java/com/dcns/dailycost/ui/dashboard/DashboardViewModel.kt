package com.dcns.dailycost.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserBalanceRepository
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val userBalanceRepository: UserBalanceRepository
): BaseViewModel<DashboardState, DashboardAction, DashboardUiEvent>() {

    init {
        viewModelScope.launch {
            userBalanceRepository.getUserBalance.collect { balance ->
                updateState {
                    copy(
                        balance = balance
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
    }

    override fun defaultState(): DashboardState = DashboardState()

    override fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.Refresh -> viewModelScope.launch {
                updateState {
                    copy(
                        isRefreshing = true
                    )
                }

                // Simulate a refresh
                launch(Dispatchers.IO) {
                    delay(2000)
                    updateState {
                        copy(
                            isRefreshing = false
                        )
                    }
                }
            }
        }
    }
}