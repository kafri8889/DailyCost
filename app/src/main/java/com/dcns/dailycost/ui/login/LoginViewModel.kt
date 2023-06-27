package com.dcns.dailycost.ui.login

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Resource
import com.dcns.dailycost.data.datasource.local.AppDatabase
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
import com.dcns.dailycost.data.model.remote.request_body.LoginRequestBody
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
class LoginViewModel @Inject constructor(
    private val userCredentialRepository: IUserCredentialRepository,
    private val userBalanceRepository: IBalanceRepository,
    private val loginRegisterUseCases: LoginRegisterUseCases,
    private val connectivityManager: ConnectivityManager,
    private val depoUseCases: DepoUseCases,
    private val appDatabase: AppDatabase
): BaseViewModel<LoginState, LoginAction>() {

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
                    sendEvent(LoginUiEvent.NoInternetConnection())
                }
            }
        }
    }

    override fun defaultState(): LoginState = LoginState()

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
                    with(userCredentialRepository) {
                        setId("")
                        setName("")
                        setEmail("")
                        setToken("")
                        setPassword("")
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
                            isValidPassword.exceptionOrNull() is PasswordValidator.DigitMissingException -> action.context.getString(R.string.digit_missing_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.BelowMinLengthException -> action.context.getString(R.string.below_min_length_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.LowerCaseMissingException -> action.context.getString(R.string.lowercase_missing_exception_msg)
                            isValidPassword.exceptionOrNull() is PasswordValidator.UpperCaseMissingException -> action.context.getString(R.string.uppercase_missing_exception_msg)
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
                    }
                }
            }
        }
    }
}