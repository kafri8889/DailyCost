package com.dcns.dailycost.ui.app

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyCostAppViewModel @Inject constructor(
    private val userCredentialRepository: IUserCredentialRepository,
    private val userPreferenceRepository: IUserPreferenceRepository,
    private val depoUseCases: DepoUseCases
): BaseViewModel<DailyCostAppState, DailyCostAppAction>() {

    init {
        viewModelScope.launch {
            userPreferenceRepository.getUserPreference.collect { pref ->
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