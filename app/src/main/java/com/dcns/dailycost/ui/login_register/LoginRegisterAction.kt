package com.dcns.dailycost.ui.login_register

import android.content.Context
import com.dcns.dailycost.data.LoginRegisterType

sealed interface LoginRegisterAction {

    data class UpdateLoginRegisterType(val type: LoginRegisterType): LoginRegisterAction
    data class UpdateShowPassword(val show: Boolean): LoginRegisterAction
    data class UpdateRememberMe(val remember: Boolean): LoginRegisterAction
    data class UpdatePasswordRe(val passwordRe: String): LoginRegisterAction
    data class UpdateUsername(val username: String): LoginRegisterAction
    data class UpdatePassword(val password: String): LoginRegisterAction
    data class UpdateEmail(val email: String): LoginRegisterAction
    data class ClearData(val context: Context): LoginRegisterAction
    data class Login(val context: Context): LoginRegisterAction

}