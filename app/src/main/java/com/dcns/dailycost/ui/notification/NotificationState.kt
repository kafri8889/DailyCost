package com.dcns.dailycost.ui.notification

import com.dcns.dailycost.data.model.Notification

data class NotificationState(
	val notifications: List<Notification> = emptyList()
)
