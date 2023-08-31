package com.dcns.dailycost.ui.notification

import com.dcns.dailycost.domain.use_case.NotificationUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
	private val notificationUseCases: NotificationUseCases
): BaseViewModel<NotificationState, NotificationAction>() {

	override fun defaultState(): NotificationState = NotificationState()

	override fun onAction(action: NotificationAction) {

	}
}