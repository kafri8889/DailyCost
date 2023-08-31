package com.dcns.dailycost.foundation.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import com.dcns.dailycost.R
import javax.inject.Inject
import kotlin.random.Random

class NotificationManager @Inject constructor(context: Context): ContextWrapper(context) {

    private val EVENT_CHANNEL_ID = "worker"
    private val EVENT_CHANNEL_NAME = "Sync worker"

    private val notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val workerChannel = NotificationChannel(
            EVENT_CHANNEL_ID,
            EVENT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(workerChannel)
    }

	fun notifyEvent(
		title: String,
		body: String,
		id: Int = Random.nextInt()
	) {
		val notif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Notification.Builder(this, EVENT_CHANNEL_ID)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(body)
				.setCategory(Notification.CATEGORY_EVENT)
				.build()
		} else {
			Notification.Builder(this)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(body)
				.setCategory(Notification.CATEGORY_EVENT)
				.build()
		}

		notificationManager.notify(id, notif)
	}

}