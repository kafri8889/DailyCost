package com.dcns.dailycost.ui.login_register

import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.R
import com.dcns.dailycost.data.LoginRegisterType
import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.data.model.networking.request_body.LoginRequestBody
import com.dcns.dailycost.data.model.networking.request_body.RegisterRequestBody
import com.dcns.dailycost.data.model.networking.response.ErrorResponse
import com.dcns.dailycost.data.repository.UserCredentialRepository
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCase
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.EmailValidator
import com.dcns.dailycost.foundation.common.PasswordValidator
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository,
    private val loginRegisterUseCase: LoginRegisterUseCase
): BaseViewModel<LoginRegisterState, LoginRegisterAction, LoginRegisterUiEvent>() {

    override fun defaultState(): LoginRegisterState = LoginRegisterState()

    override fun onAction(action: LoginRegisterAction) {
        when (action) {
            is LoginRegisterAction.UpdateLoginRegisterType -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            loginRegisterType = action.type,
                            email = "",
                            emailError = null,
                            password = "",
                            passwordError = null,
                            showPassword = false,
                            rememberMe = false
                        )
                    }
                }
            }
            is LoginRegisterAction.UpdateShowPassword -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            showPassword = action.show
                        )
                    }
                }
            }
            is LoginRegisterAction.UpdateEmail -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            email = action.email,
                            emailError = null // Clear error when email is changed
                        )
                    }
                }
            }
            is LoginRegisterAction.UpdatePassword -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            password = action.password,
                            passwordError = null // Clear error when password is changed
                        )
                    }
                }
            }
            is LoginRegisterAction.UpdateRememberMe -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            rememberMe = action.remember
                        )
                    }
                }
            }
            is LoginRegisterAction.UpdatePasswordRe -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            passwordRe = action.passwordRe
                        )
                    }
                }
            }
            is LoginRegisterAction.UpdateUsername -> {
                viewModelScope.launch {
                    updateState {
                        copy(
                            username = action.username
                        )
                    }
                }
            }
            is LoginRegisterAction.Login -> {
                viewModelScope.launch {
                    val mState = state.value
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
                            isValidPassword.exceptionOrNull() is PasswordValidator.DigitMissingException -> action.context.getString(R.string.digit_missing_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.BelowMinLengthException -> action.context.getString(R.string.below_min_length_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.LowerCaseMissingException -> action.context.getString(R.string.lowercase_missing_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.UpperCaseMissingException -> action.context.getString(R.string.uppercase_missing_exception_msg)
                            !mState.password.contentEquals(mState.passwordRe) -> action.context.getString(R.string.password_does_not_match)
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
                        val requestBody = if (mState.loginRegisterType == LoginRegisterType.Login) {
                            LoginRequestBody(
                                email = mState.email,
                                password = mState.password
                            ).toRequestBody()
                        } else {
                            RegisterRequestBody(
                                name = mState.username,
                                email = mState.email,
                                password = mState.password
                            ).toRequestBody()
                        }

                        updateState {
                            copy(
                                resource = Resource.loading(null)
                            )
                        }

                        loginRegisterUseCase(
                            type = mState.loginRegisterType,
                            body = requestBody
                        ).let { response ->
                            updateState {
                                copy(
                                    resource = if (response.isSuccessful) {
                                        // TODO: Update user credentials

                                        Resource.success(response.body())
                                    } else {
                                        val errorResponse = Gson().fromJson(response.errorBody()?.charStream(), ErrorResponse::class.java)

                                        Resource.error(errorResponse.message, errorResponse)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}