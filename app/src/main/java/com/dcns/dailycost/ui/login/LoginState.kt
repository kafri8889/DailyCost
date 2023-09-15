package com.dcns.dailycost.ui.login

data class LoginState(
	val internetConnectionAvailable: Boolean = true,
	/**
	 * Ketika user menekan tombol login dan sedang menunggu respone API
	 */
	val isLoading: Boolean = false,
	/**
	 * `true` jika login berhasil, false otherwise
	 */
	val isSuccess: Boolean = false,
	val isFirstInstall: Boolean = false,
	val showPassword: Boolean = false,
	val rememberMe: Boolean = false,
	val passwordError: String? = null,
	val emailError: String? = null,
	val password: String = "",
	val email: String = "",
)
