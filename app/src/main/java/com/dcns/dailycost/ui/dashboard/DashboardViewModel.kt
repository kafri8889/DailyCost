package com.dcns.dailycost.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.domain.use_case.NoteUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val balanceUseCases: BalanceUseCases,
    private val noteUseCases: NoteUseCases
): BaseViewModel<DashboardState, DashboardAction, DashboardUiEvent>() {

    init {
        viewModelScope.launch {
            noteUseCases.getLocalNoteUseCase().collect { notes ->
                updateState {
                    copy(
                        recentNotes = notes.take(2)
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