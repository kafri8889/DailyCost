package com.dcns.dailycost.ui.login_register

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.R
import com.dcns.dailycost.data.LoginRegisterType
import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.data.datasource.local.AppDatabase
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.request_body.LoginRequestBody
import com.dcns.dailycost.data.model.remote.request_body.RegisterRequestBody
import com.dcns.dailycost.data.model.remote.response.ErrorResponse
import com.dcns.dailycost.data.model.remote.response.LoginResponse
import com.dcns.dailycost.domain.repository.IBalanceRepository
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.use_case.DepoUseCases
import com.dcns.dailycost.domain.use_case.LoginRegisterUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.EmailValidator
import com.dcns.dailycost.foundation.common.PasswordValidator
import com.dcns.dailycost.foundation.common.Workers
import com.dcns.dailycost.foundation.extension.enqueue
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val userCredentialRepository: IUserCredentialRepository,
    private val userBalanceRepository: IBalanceRepository,
    private val loginRegisterUseCases: LoginRegisterUseCases,
    private val connectivityManager: ConnectivityManager,
    private val depoUseCases: DepoUseCases,
    private val appDatabase: AppDatabase
): BaseViewModel<LoginRegisterState, LoginRegisterAction, LoginRegisterUiEvent>() {

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
                    sendEvent(LoginRegisterUiEvent.NoInternetConnection())
                }
            }
        }
    }

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
            is LoginRegisterAction.ClearData -> {
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
                    with(userCredentialRepository) {
                        setId("")
                        setName("")
                        setEmail("")
                        setToken("")
                        setPassword("")
                    }
                }
            }
            is LoginRegisterAction.Login -> {
                viewModelScope.launch {
                    val mState = state.value

                    // Kalo ga ada koneksi internet, show snackbar
                    if (!mState.internetConnectionAvailable) {
                        sendEvent(LoginRegisterUiEvent.NoInternetConnection())
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
                        updateState {
                            copy(
                                resource = Resource.loading(null)
                            )
                        }

                        if (mState.loginRegisterType == LoginRegisterType.Login) {
                            loginRegisterUseCases.userLoginUseCase(
                                LoginRequestBody(
                                    email = mState.email,
                                    password = mState.password
                                ).toRequestBody()
                            ).let { response ->
                               if (response.isSuccessful) {
                                   val body = response.body() as LoginResponse

                                   if (mState.rememberMe) {
                                       launch {
                                           with(userCredentialRepository) {
                                               setName(body.data.name)
                                               setEmail(mState.email)
                                               setPassword(mState.password)
                                           }
                                       }
                                   }

                                   with(userCredentialRepository) {
                                       setId(body.data.id.toString())
                                       setToken(body.token)
                                   }

                                   depoUseCases.addDepoUseCase(
                                       token = "Bearer ${body.token}",
                                       body = DepoRequestBody(
                                           id = body.data.id,
                                           cash = 0,
                                           eWallet = 0,
                                           bankAccount = 0
                                       ).toRequestBody()
                                   )

                                   updateState {
                                       copy(
                                           resource = Resource.success(response.body())
                                       )
                                   }

                                   Workers.syncWorker().enqueue(action.context)

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

                            return@launch
                        }

                        // loginRegisterType == LoginRegisterType.Register
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
                                        username = "",
                                        passwordRe = "",
                                        loginRegisterType = LoginRegisterType.Login
                                    )
                                }

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