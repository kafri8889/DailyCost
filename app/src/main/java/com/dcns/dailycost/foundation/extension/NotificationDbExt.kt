package com.dcns.dailycost.foundation.extension

import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.data.model.local.NotificationDb

fun NotificationDb.toNotification(): Notification {
	return Notification(id, title, body, url, hasBeenRead, sentTimeMillis)
}

fun Notification.toNotificationDb(): NotificationDb {
	return NotificationDb(id, title, body, url, hasBeenRead, sentTimeMillis)
}
