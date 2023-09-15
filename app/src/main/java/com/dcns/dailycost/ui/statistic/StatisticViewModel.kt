package com.dcns.dailycost.ui.statistic

import androidx.lifecycle.SavedStateHandle
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle
): BaseViewModel<StatisticState, StatisticAction>(savedStateHandle, StatisticState()) {

	init {

	}

	override fun onAction(action: StatisticAction) {

	}
}