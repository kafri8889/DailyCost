package com.dcns.dailycost.domain.use_case.notification

import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.domain.repository.INotificationRepository
import com.dcns.dailycost.domain.util.GetNotificationBy
import com.dcns.dailycost.foundation.extension.toNotification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Use case untuk mendapatkan notifikasi dari db
 */
class GetLocalNotificationUseCase(
	private val notificationRepository: INotificationRepository
) {

	operator fun invoke(
		getNotificationBy: GetNotificationBy = GetNotificationBy.All
	): Flow<List<Notification>> {
		val flow = when (getNotificationBy) {
			GetNotificationBy.All -> notificationRepository.getAllNotification()
		}

		return flow.filterNotNull()
			.map { it.map { it.toNotification() } }
	}

}