package com.dcns.dailycost.ui.recent_activity

import androidx.lifecycle.SavedStateHandle
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentActivityViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle
): BaseViewModel<RecentActivityState, RecentActivityAction>(savedStateHandle, RecentActivityState()) {

	override fun onAction(action: RecentActivityAction) {

	}
}