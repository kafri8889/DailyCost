package com.dcns.dailycost.ui.register

import android.content.Context

sealed interface RegisterAction {

	data class UpdateShowPassword(val show: Boolean): RegisterAction
	data class UpdateUsername(val username: String): RegisterAction
	data class UpdatePassword(val password: String): RegisterAction
	data class UpdateEmail(val email: String): RegisterAction
	data class SignUp(val context: Context): RegisterAction

}