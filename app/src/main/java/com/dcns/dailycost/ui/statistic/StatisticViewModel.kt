package com.dcns.dailycost.ui.statistic

import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(

): BaseViewModel<StatisticState, StatisticAction>() {

	init {

	}

	override fun defaultState(): StatisticState = StatisticState()

	override fun onAction(action: StatisticAction) {

	}
}