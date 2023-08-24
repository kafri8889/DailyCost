package com.dcns.dailycost.foundation.localized

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.util.EditUserPreferenceType
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalizedViewModel @Inject constructor(
	private val userPreferenceUseCases: UserPreferenceUseCases
): BaseViewModel<LocalizedState, LocalizedAction>() {

	init {
		viewModelScope.launch {
			userPreferenceUseCases.getUserPreferenceUseCase().collect { pref ->
				sendEvent(LocalizedUiEvent.LanguageChanged(pref.language))
				sendEvent(LocalizedUiEvent.ApplyLanguage(pref.language))
				updateState {
					copy(
						language = pref.language
					)
				}
			}
		}
	}

	override fun defaultState(): LocalizedState = LocalizedState()

	override fun onAction(action: LocalizedAction) {
		when (action) {
			is LocalizedAction.SetLanguage -> {
				viewModelScope.launch {
					userPreferenceUseCases.editUserPreferenceUseCase(
						type = EditUserPreferenceType.Language(action.lang)
					)
				}
			}
		}
	}
}