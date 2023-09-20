package com.dcns.dailycost.ui.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.data.model.UserCredential
import com.dcns.dailycost.domain.use_case.CommonUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.util.EditUserCredentialType
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DailyCostAppViewModel @Inject constructor(
	private val userPreferenceUseCases: UserPreferenceUseCases,
	private val userCredentialUseCases: UserCredentialUseCases,
	private val commonUseCases: CommonUseCases,
	private val connectivityManager: ConnectivityManager,
	private val savedStateHandle: SavedStateHandle
): BaseViewModel<DailyCostAppState, DailyCostAppAction>(savedStateHandle, DailyCostAppState()) {

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
			userCredentialUseCases.getUserCredentialUseCase().combine(
				connectivityManager.isNetworkAvailable.asFlow()
			) { cred, have ->
				cred to have
			}.collect { (cred, have) ->
				Timber.i("credential: $cred | ${cred.isLoggedIn}")

				if (have == true) checkToken(cred)
				else {
					updateState {
						copy(
							userCredential = cred
						)
					}
				}
			}
		}
	}

	private suspend fun checkToken(credential: UserCredential) {
		updateState {
			copy(
				userCredential = credential
			)
		}

		if (commonUseCases.checkTokenExpiredUseCase()) {
			sendEvent(DailyCostAppUiEvent.TokenExpired)

			// Clear credential
			with(userCredentialUseCases.editUserCredentialUseCase) {
				invoke(EditUserCredentialType.ID(""))
				invoke(EditUserCredentialType.Name(""))
				invoke(EditUserCredentialType.Email(""))
				invoke(EditUserCredentialType.Token(""))
				invoke(EditUserCredentialType.Password(""))
			}
		}
	}

	override fun onAction(action: DailyCostAppAction) {
		when (action) {
			is DailyCostAppAction.UpdateCurrentDestinationRoute -> {
				// Save the current route to saveStateHandle so it can survive system process death
				updateState {
					copy(
						currentDestinationRoute = action.route
					)
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
			is DailyCostAppAction.CanNavigate -> {
				viewModelScope.launch {
					updateState {
						copy(
							canNavigate = action.can
						)
					}
				}
			}
		}
	}
}