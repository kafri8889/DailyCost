package com.dcns.dailycost.ui.setting

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
): BaseViewModel<SettingState, SettingAction, SettingUiEvent>() {

    init {
        viewModelScope.launch {
            userCredentialRepository.getUserCredential.collect { cred ->
                updateState {
                    copy(
                        userCredential = cred
                    )
                }
            }
        }
    }

    override fun defaultState(): SettingState = SettingState()

    override fun onAction(action: SettingAction) {
        when (action) {
            SettingAction.Logout -> {
                viewModelScope.launch {
                    with(userCredentialRepository) {
                        setId(-1)
                        setName("")
                        setEmail("")
                        setToken("")
                        setPassword("")
                    }
                }
            }
        }
    }
}