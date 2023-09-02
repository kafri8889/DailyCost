package com.dcns.dailycost.ui.notification

import com.dcns.dailycost.data.model.Notification

sealed interface NotificationAction {

	data class UpdateNotification(val notification: Notification): NotificationAction

}