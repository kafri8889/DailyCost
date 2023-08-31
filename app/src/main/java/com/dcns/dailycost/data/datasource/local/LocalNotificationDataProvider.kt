package com.dcns.dailycost.data.datasource.local

import com.dcns.dailycost.data.model.Notification

object LocalNotificationDataProvider {

	val notification1 = Notification(
		id = 0,
		title = "Release 1.0.0-alpha01",
		body = "Aplikasi daily cost telah dirilis untuk preview",
		hasBeenRead = true
	)

	val notification2 = Notification(
		id = 0,
		title = "Release 1.0.0-alpha02",
		body = "Aplikasi daily cost telah mengeluarkan fitur baru di preview alpha02",
		hasBeenRead = true
	)

	val values = arrayOf(
		notification1,
		notification2,
	)

}