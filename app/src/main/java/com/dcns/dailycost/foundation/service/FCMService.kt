package com.dcns.dailycost.foundation.service

import android.content.Intent
import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.domain.use_case.NotificationUseCases
import com.dcns.dailycost.foundation.common.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FCMService: FirebaseMessagingService() {

	@Inject
	lateinit var notificationManager: NotificationManager

	@Inject
	lateinit var notificationUseCases: NotificationUseCases

	private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)

		val title = message.data["title"]
		val body = message.data["body"]

		if (!title.isNullOrBlank() && !body.isNullOrBlank()) {
			Notification(
				id = Random.nextInt(),
				title = title,
				body = body
			).also {
				// Save notifikasi ke database
				scope.launch { notificationUseCases.insertLocalNotificationUseCase(it) }
			}

			return
		}

		message.notification?.let { notification ->
			if (!notification.title.isNullOrBlank() && !notification.body.isNullOrBlank()) {
				// Push notification
				notificationManager.notifyEvent(
					notification = Notification(
						id = Random.nextInt(),
						title = notification.title!!,
						body = notification.body!!
					).also {
						// Save notifikasi ke database
						scope.launch { notificationUseCases.insertLocalNotificationUseCase(it) }
					}
				)
			}
		}
	}

	override fun handleIntent(intent: Intent?) {
		val builder = RemoteMessage.Builder("FCMService").apply {
			addData("title", intent?.getStringExtra("title") ?: "")
			addData("body", intent?.getStringExtra("body") ?: "")
		}

		onMessageReceived(builder.build())
		super.handleIntent(intent)
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
	}
}