package com.dcns.dailycost.ui.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterState(
	val internetConnectionAvailable: Boolean = true,
	/**
	 * Ketika user menekan tombol register dan sedang menunggu respone API
	 */
	val isLoading: Boolean = false,
	/**
	 * `true` jika register berhasil, false otherwise
	 */
	val isSuccess: Boolean = false,
	val showPassword: Boolean = false,
	val passwordError: String? = null,
	val usernameError: String? = null,
	val emailError: String? = null,
	val password: String = "",
	val email: String = "",
	val username: String = "",
): Parcelable
