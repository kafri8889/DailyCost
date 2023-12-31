package com.dcns.dailycost.ui.change_language

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.util.EditUserPreferenceType
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLanguageViewModel @Inject constructor(
	private val userPreferenceUseCases: UserPreferenceUseCases,
	savedStateHandle: SavedStateHandle
): BaseViewModel<ChangeLanguageState, ChangeLanguageAction>(savedStateHandle, ChangeLanguageState()) {

	init {
		viewModelScope.launch {
			userPreferenceUseCases.getUserPreferenceUseCase().collect { pref ->
				updateState {
					copy(
						selectedLanguage = pref.language
					)
				}
			}
		}
	}

	override fun onAction(action: ChangeLanguageAction) {
		when (action) {
			is ChangeLanguageAction.ChangeLanguage -> {
				viewModelScope.launch {
					userPreferenceUseCases.editUserPreferenceUseCase(
						type = EditUserPreferenceType.Language(action.language)
					)
				}
			}
		}
	}
}