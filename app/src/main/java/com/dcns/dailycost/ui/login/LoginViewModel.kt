package com.dcns.dailycost.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.dcns.dailycost.R
import com.dcns.dailycost.data.datasource.local.AppDatabase
import com.dcns.dailycost.data.model.remote.request_body.LoginRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.domain.use_case.UserPreferenceUseCases
import com.dcns.dailycost.domain.util.EditUserCredentialType
import com.dcns.dailycost.domain.util.EditUserPreferenceType
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.base.UiEvent
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.EmailValidator
import com.dcns.dailycost.foundation.common.PasswordValidator
import com.dcns.dailycost.foundation.worker.Workers
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val userPreferenceUseCases: UserPreferenceUseCases,
	private val userCredentialUseCases: UserCredentialUseCases,
	private val userBalanceRepository: IBalanceRepository,
	private val loginRegisterUseCases: LoginRegisterUseCases,
	private val connectivityManager: ConnectivityManager,
	private val appDatabase: AppDatabase,
	private val workManager: WorkManager
): BaseViewModel<LoginState, LoginAction>(savedStateHandle, LoginState()) {

	init {
		viewModelScope.launch {
			connectivityManager.isNetworkAvailable.asFlow().collect { have ->
				Timber.i("have internet: $have")

				updateState {
					copy(
						internetConnectionAvailable = have == true
					)
				}

				// Kalo ga ada koneksi internet, show snackbar
				if (have == false) {
					sendEvent(LoginUiEvent.NoInternetConnection())
				}
			}
		}

		viewModelScope.launch {
			userPreferenceUseCases.getUserPreferenceUseCase().collect { pref ->
				updateState {
					copy(
						isFirstInstall = !pref.isNotFirstInstall
					)
				}
			}
		}
	}

	/**
	 * For testing purposes.
	 *
	 * Mensimulasikan login
	 */
	private fun performLogin() {
		viewModelScope.launch {
			with(userCredentialUseCases.editUserCredentialUseCase) {
				invoke(EditUserCredentialType.Name("Guest"))
				invoke(EditUserCredentialType.Email("guest@example.com"))
				invoke(EditUserCredentialType.Password("Guest123"))
				invoke(EditUserCredentialType.ID("-1"))
				invoke(EditUserCredentialType.Token("token"))
			}

			updateState {
				copy(
					isSuccess = true,
					isLoading = false
				)
			}
		}
	}

	override fun onAction(action: LoginAction) {
		when (action) {
			is LoginAction.UpdateEmail -> {
				viewModelScope.launch {
					updateState {
						copy(
							email = action.email,
							emailError = null // Clear error when email is changed
						)
					}
				}
			}

			is LoginAction.UpdatePassword -> {
				viewModelScope.launch {
					updateState {
						copy(
							password = action.password,
							passwordError = null // Clear error when password is changed
						)
					}
				}
			}

			is LoginAction.UpdateRememberMe -> {
				viewModelScope.launch {
					updateState {
						copy(
							rememberMe = action.remember
						)
					}
				}
			}

			is LoginAction.UpdateShowPassword -> {
				viewModelScope.launch {
					updateState {
						copy(
							showPassword = action.show
						)
					}
				}
			}

			is LoginAction.ClearData -> {
				viewModelScope.launch(Dispatchers.IO) {
					// Clear database
					appDatabase.clearAllTables()

					// Clear cache
					action.context.cacheDir.deleteRecursively()

					// Clear balance
					with(userBalanceRepository) {
						setCash(0.0)
						setEWallet(0.0)
						setBankAccount(0.0)
					}

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

			is LoginAction.SignIn -> {
				viewModelScope.launch {
					val mState = state.value

					// Kalo ga ada koneksi internet, show snackbar
					if (!mState.internetConnectionAvailable) {
						sendEvent(LoginUiEvent.NoInternetConnection())
						return@launch
					}

					val isValidEmail = EmailValidator.validate(mState.email)
					val isValidPassword = PasswordValidator.validate(mState.password)

					val emailErrorMessage = if (isValidEmail.isFailure) {
						when {
							mState.email.isBlank() -> action.context.getString(R.string.email_cant_be_empty)
							else -> action.context.getString(R.string.email_not_valid)
						}
					} else null

					val passwordErrorMessage = if (isValidPassword.isFailure) {
						when {
							mState.password.isBlank() -> action.context.getString(R.string.password_cant_be_empty)
							isValidPassword.exceptionOrNull() is PasswordValidator.DigitMissingException -> action.context.getString(
								R.string.digit_missing_exception_msg
							)

							isValidPassword.exceptionOrNull() is PasswordValidator.BelowMinLengthException -> action.context.getString(
								R.string.below_min_length_exception_msg
							)

							isValidPassword.exceptionOrNull() is PasswordValidator.LowerCaseMissingException -> action.context.getString(
								R.string.lowercase_missing_exception_msg
							)

							isValidPassword.exceptionOrNull() is PasswordValidator.UpperCaseMissingException -> action.context.getString(
								R.string.uppercase_missing_exception_msg
							)

							else -> null
						}
					} else null

					updateState {
						copy(
							emailError = emailErrorMessage,
							passwordError = passwordErrorMessage
						)
					}

					// Jika emailErrorMessage dan passwordErrorMessage null
					// Berarti tidak ada error, langsung login ke api
					if (emailErrorMessage == null && passwordErrorMessage == null) {
						updateState {
							copy(
								isLoading = true
							)
						}

						try {
							loginRegisterUseCases.userLoginUseCase(
								LoginRequestBody(
									email = mState.email,
									password = mState.password
								).toRequestBody()
							).let { response ->
								withContext(Dispatchers.Main) {
									if (response.isSuccessful) {
										val body = response.body() as LoginResponse

										if (mState.rememberMe) {
											with(userCredentialUseCases.editUserCredentialUseCase) {
												invoke(EditUserCredentialType.Name(body.data.name))
												invoke(EditUserCredentialType.Email(mState.email))
												invoke(EditUserCredentialType.Password(mState.password))
											}
										}

										with(userCredentialUseCases.editUserCredentialUseCase) {
											invoke(EditUserCredentialType.ID(body.data.id.toString()))
											invoke(EditUserCredentialType.Token(body.token))
										}

										userPreferenceUseCases.editUserPreferenceUseCase(
											type = EditUserPreferenceType.IsNotFirstInstall(true)
										)

										updateState {
											copy(
												isSuccess = true,
												isLoading = false
											)
										}

										workManager.enqueue(Workers.syncWorker())

										return@withContext
									}

									// Response not success

									val errorResponse = Gson().fromJson(
										response.errorBody()?.charStream(),
										ErrorResponse::class.java
									)

									sendEvent(LoginUiEvent.Error(errorResponse.message))
									updateState {
										copy(
											isSuccess = false,
											isLoading = false
										)
									}
								}
							}
						} catch (e: SocketTimeoutException) {
							Timber.e(e, "Socket time out")
							sendEvent(LoginUiEvent.Error(UiEvent.asStringResource(R.string.connection_time_out)))
							updateState {
								copy(
									isSuccess = false,
									isLoading = false
								)
							}
						} catch (e: Exception) {
							Timber.e(e)
						}
					}
				}
			}
		}
	}
}