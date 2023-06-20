package com.dcns.dailycost.ui.app

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyCostAppViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
): BaseViewModel<DailyCostAppState, DailyCostAppAction, DailyCostAppUiEvent>() {

    init {
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