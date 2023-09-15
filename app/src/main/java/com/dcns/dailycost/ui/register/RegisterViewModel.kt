package com.dcns.dailycost.ui.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.R
import com.dcns.dailycost.data.model.remote.request_body.RegisterRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.base.UiEvent
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.EmailValidator
import com.dcns.dailycost.foundation.common.PasswordValidator
import com.dcns.dailycost.ui.login.LoginUiEvent
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val loginRegisterUseCases: LoginRegisterUseCases,
	private val connectivityManager: ConnectivityManager,
): BaseViewModel<RegisterState, RegisterAction>(savedStateHandle, RegisterState()) {

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
					sendEvent(RegisterUiEvent.NoInternetConnection())
				}
			}
		}
	}

	override fun onAction(action: RegisterAction) {
		when (action) {
			is RegisterAction.UpdateEmail -> {
				viewModelScope.launch {
					updateState {
						copy(
							email = action.email,
							emailError = null // Clear error when email is changed
						)
					}
				}
			}

			is RegisterAction.UpdatePassword -> {
				viewModelScope.launch {
					updateState {
						copy(
							password = action.password,
							passwordError = null // Clear error when password is changed
						)
					}
				}
			}

			is RegisterAction.UpdateUsername -> {
				viewModelScope.launch {
					updateState {
						copy(
							username = action.username,
						)
					}
				}
			}

			is RegisterAction.UpdateShowPassword -> {
				viewModelScope.launch {
					updateState {
						copy(
							showPassword = action.show
						)
					}
				}
			}

			is RegisterAction.SignUp -> {
				viewModelScope.launch {
					val mState = state.value

					// Kalo ga ada koneksi internet, show snackbar
					if (!mState.internetConnectionAvailable) {
						sendEvent(RegisterUiEvent.NoInternetConnection())
						return@launch
					}

					val isValidEmail = EmailValidator.validate(mState.email)
					val isValidPassword = PasswordValidator.validate(mState.password)
					val isValidUsername = mState.username.length >= 3

					val usernameErrorMessage = if (!isValidUsername) {
						action.context.getString(R.string.username_min_length_exception_msg, "3")
					} else null

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
							passwordError = passwordErrorMessage,
							usernameError = usernameErrorMessage
						)
					}

					// Jika emailErrorMessage, passwordErrorMessage, dan usernameErrorMessage null
					// Berarti tidak ada error, langsung login ke api
					if (emailErrorMessage == null && passwordErrorMessage == null && usernameErrorMessage == null) {
						updateState {
							copy(
								isLoading = true
							)
						}

						try {
							loginRegisterUseCases.userRegisterUseCase(
								RegisterRequestBody(
									name = mState.username,
									email = mState.email,
									password = mState.password
								).toRequestBody()
							).let { response ->
								if (response.isSuccessful) {
									updateState {
										copy(
											isSuccess = true,
											isLoading = false
										)
									}

									return@launch
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