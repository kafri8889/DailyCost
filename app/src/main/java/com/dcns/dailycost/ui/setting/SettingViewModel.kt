package com.dcns.dailycost.ui.setting

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.util.EditUserPreferenceType
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	private val userCredentialUseCases: UserCredentialUseCases,
	private val userPreferenceUseCases: UserPreferenceUseCases,
): BaseViewModel<SettingState, SettingAction>() {

	init {
		viewModelScope.launch {
			userCredentialUseCases.getUserCredentialUseCase().collect { cred ->
				updateState {
					copy(
						userCredential = cred
					)
				}
			}
		}

		viewModelScope.launch {
			userPreferenceUseCases.getUserPreferenceUseCase().collect { pref ->
				updateState {
					copy(
						language = pref.language,
						isSecureAppEnabled = pref.secureApp,
						defaultBalanceVisibility = pref.defaultBalanceVisibility
					)
				}
			}
		}
	}

	override fun defaultState(): SettingState = SettingState()

	override fun onAction(action: SettingAction) {
		when (action) {
			is SettingAction.UpdateIsSecureAppEnabled -> {
				viewModelScope.launch {
					userPreferenceUseCases.editUserPreferenceUseCase(
						type = EditUserPreferenceType.SecureApp(action.enabled)
					)
				}
			}
			is SettingAction.UpdateDefaultBalanceVisibility -> viewModelScope.launch {
				userPreferenceUseCases.editUserPreferenceUseCase(
					type = EditUserPreferenceType.DefaultBalanceVisibility(action.visible)
				)
			}
		}
	}
}