package com.dcns.dailycost.domain.use_case.notification

import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.domain.repository.INotificationRepository
import com.dcns.dailycost.foundation.extension.toNotificationDb

/**
 * Use case untuk memperbarui notifikasi ke db
 */
class UpdateLocalNotificationUseCase(
	private val notificationRepository: INotificationRepository
) {

	suspend operator fun invoke(vararg notification: Notification) {
		notificationRepository.updateNotification(*notification.map { it.toNotificationDb() }.toTypedArray())
	}

}
