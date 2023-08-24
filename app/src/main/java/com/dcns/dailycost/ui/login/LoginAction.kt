package com.dcns.dailycost.ui.login

import android.content.Context

sealed interface LoginAction {

	data class UpdateShowPassword(val show: Boolean): LoginAction
	data class UpdateRememberMe(val remember: Boolean): LoginAction
	data class UpdatePassword(val password: String): LoginAction
	data class UpdateEmail(val email: String): LoginAction
	data class ClearData(val context: Context): LoginAction
	data class SignIn(val context: Context): LoginAction

}