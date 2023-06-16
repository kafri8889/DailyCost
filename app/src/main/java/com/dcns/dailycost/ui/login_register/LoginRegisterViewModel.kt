package com.dcns.dailycost.ui.login_register

import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(

): BaseViewModel<LoginRegisterState, LoginRegisterAction, LoginRegisterUiEvent>() {

    override fun defaultState(): LoginRegisterState = LoginRegisterState()

    override fun onAction(action: LoginRegisterAction) {

    }
}