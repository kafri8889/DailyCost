package com.dcns.dailycost.ui.app

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyCostAppViewModel @Inject constructor(
    private val userPreferenceUseCases: UserPreferenceUseCases,
    private val userCredentialUseCases: UserCredentialUseCases,
    private val depoUseCases: DepoUseCases
): BaseViewModel<DailyCostAppState, DailyCostAppAction>() {

    init {
        viewModelScope.launch {
            userPreferenceUseCases.getUserPreferenceUseCase().collect { pref ->
                updateState {
                    copy(
                        isSecureAppEnabled = pref.secureApp,
                        isFirstInstall = !pref.isNotFirstInstall,
                        language = pref.language
                    )
                }
            }
        }

        viewModelScope.launch {
            depoUseCases.getLocalBalanceUseCase().collect { balance ->
                updateState {
                    copy(
                        userBalance = balance
                    )
                }
            }
        }

        viewModelScope.launch {
            userCredentialUseCases.getUserCredentialUseCase().collect { cred ->
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
            is DailyCostAppAction.IsBiometricAuthenticated -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            isBiometricAuthenticated = action.authenticated
                        )
                    }
                }
            }
            is DailyCostAppAction.UpdateUserFirstEnteredApp -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            userFirstEnteredApp = action.first
                        )
                    }
                }
            }
        }
    }
}