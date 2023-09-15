package com.dcns.dailycost.ui.notification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dcns.dailycost.domain.use_case.NotificationUseCases
import com.dcns.dailycost.foundation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val notificationUseCases: NotificationUseCases
): BaseViewModel<NotificationState, NotificationAction>(savedStateHandle, NotificationState()) {

	init {
	    viewModelScope.launch {
			notificationUseCases.getLocalNotificationUseCase().collect { notifications ->
				updateState {
					copy(
						notifications = notifications
					)
				}
			}
		}
	}

	override fun onAction(action: NotificationAction) {
		when (action) {
			is NotificationAction.UpdateNotification -> {
				viewModelScope.launch(Dispatchers.IO) {
					notificationUseCases.updateLocalNotificationUseCase(action.notification)
				}
			}
		}
	}
}