package com.dcns.dailycost.ui.register

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.data.model.remote.request_body.RegisterRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.repository.IUserPreferenceRepository
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.EmailValidator
import com.dcns.dailycost.foundation.common.PasswordValidator
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userCredentialRepository: IUserCredentialRepository,
    private val userPreferenceRepository: IUserPreferenceRepository,
    private val userBalanceRepository: IBalanceRepository,
    private val loginRegisterUseCases: LoginRegisterUseCases,
    private val connectivityManager: ConnectivityManager,
): BaseViewModel<RegisterState, RegisterAction>() {

    init {
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.asFlow().collect { have ->
                Timber.i("have internet: $have")

                updateState {
                    copy(
                        internetConnectionAvailable = have
                    )
                }

                // Kalo ga ada koneksi internet, show snackbar
                if (!have) {
                    sendEvent(RegisterUiEvent.NoInternetConnection())
                }
            }
        }
    }

    override fun defaultState(): RegisterState = RegisterState()

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
                                R.string.digit_missing_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.BelowMinLengthException -> action.context.getString(
                                R.string.below_min_length_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.LowerCaseMissingException -> action.context.getString(
                                R.string.lowercase_missing_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.UpperCaseMissingException -> action.context.getString(
                                R.string.uppercase_missing_exception_msg)
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
                                resource = Resource.loading(null)
                            )
                        }

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
                                        resource = Resource.success(response.body())
                                    )
                                }

                                return@launch
                            }

                            // Response not success

                            val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)

                            updateState {
                                copy(
                                    resource = Resource.error(errorResponse.message, errorResponse)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}