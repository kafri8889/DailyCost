package com.dcns.dailycost.domain.use_case.notification

import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.domain.repository.INotificationRepository
import com.dcns.dailycost.foundation.extension.toNotificationDb

/**
 * Use case untuk memasukan notifikasi ke db
 */
class InsertLocalNotificationUseCase(
	private val notificationRepository: INotificationRepository
) {

	suspend operator fun invoke(vararg notification: Notification) {
		notificationRepository.insertNotification(*notification.map { it.toNotificationDb() }.toTypedArray())
	}

}