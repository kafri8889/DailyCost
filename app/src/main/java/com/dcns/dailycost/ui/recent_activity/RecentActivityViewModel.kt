package com.dcns.dailycost.ui.recent_activity

import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentActivityViewModel @Inject constructor(

): BaseViewModel<RecentActivityState, RecentActivityAction>() {

	init {

	}

	override fun defaultState(): RecentActivityState = RecentActivityState()

	override fun onAction(action: RecentActivityAction) {

	}
}