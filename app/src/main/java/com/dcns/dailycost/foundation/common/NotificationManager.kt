package com.dcns.dailycost.foundation.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.dcns.dailycost.MainActivity
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.DestinationRoute
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
		notification: com.dcns.dailycost.data.model.Notification,
		id: Int = Random.nextInt()
	) {
		val deepLinkIntent = Intent(
			Intent.ACTION_VIEW,
			"${Constant.APP_DEEP_LINK_SCHEME}://${Constant.APP_DEEP_LINK_HOST}/${DestinationRoute.NOTIFICATION}".toUri(),
			this,
			MainActivity::class.java
		).apply {
			flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
		}

		val pi = PendingIntent.getActivity(
			this,
			0,
			deepLinkIntent,
			PendingIntent.FLAG_UPDATE_CURRENT
		)

		val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(this, EVENT_CHANNEL_ID)
		else Notification.Builder(this)

		val notif = builder
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(notification.title)
				.setContentText(notification.body)
				.setCategory(Notification.CATEGORY_EVENT)
				.setContentIntent(pi)
				.setAutoCancel(true)
				.build()


		notificationManager.notify(id, notif)
	}

}