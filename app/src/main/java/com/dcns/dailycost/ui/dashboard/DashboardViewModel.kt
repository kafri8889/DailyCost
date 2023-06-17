package com.dcns.dailycost.ui.dashboard

import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(

): BaseViewModel<DashboardState, DashboardAction, DashboardUiEvent>() {

    override fun defaultState(): DashboardState = DashboardState()

    override fun onAction(action: DashboardAction) {

    }
}