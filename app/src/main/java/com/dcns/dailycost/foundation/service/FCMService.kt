package com.dcns.dailycost.foundation.service

import com.dcns.dailycost.foundation.common.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FCMService: FirebaseMessagingService() {

	@Inject
	lateinit var notificationManager: NotificationManager

	override fun onMessageReceived(message: RemoteMessage) {

		message.notification?.let { notification ->
			notificationManager.notifyEvent(
				title = notification.title ?: "",
				body = notification.body ?: ""
			)
		}

		super.onMessageReceived(message)
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
	}
}