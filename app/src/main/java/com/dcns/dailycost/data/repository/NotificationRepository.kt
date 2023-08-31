package com.dcns.dailycost.data.repository

import com.dcns.dailycost.data.datasource.local.dao.NotificationDao
import com.dcns.dailycost.data.model.local.NotificationDb
import com.dcns.dailycost.domain.repository.INotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepository @Inject constructor(
	private val notificationDao: NotificationDao
): INotificationRepository {

	override fun getAllNotification(): Flow<List<NotificationDb>> {
		return notificationDao.getAllNotification()
	}

	override suspend fun updateNotification(vararg notificationDb: NotificationDb) {
		notificationDao.updateNotification(*notificationDb)
	}

	override suspend fun insertNotification(vararg notificationDb: NotificationDb) {
		notificationDao.insertNotification(*notificationDb)
	}
}