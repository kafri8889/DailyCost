package com.dcns.dailycost.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
): BaseViewModel<DashboardState, DashboardAction, DashboardUiEvent>() {

    init {
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

    }
}