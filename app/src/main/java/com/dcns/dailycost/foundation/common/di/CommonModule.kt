package com.dcns.dailycost.foundation.common.di

import android.content.Context
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.EncryptionManager
import com.dcns.dailycost.foundation.common.NotificationManager
import com.dcns.dailycost.foundation.common.SharedData
import com.dcns.dailycost.foundation.common.SharedUiEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

	@Provides
	@Singleton
	fun provideConnectivityManager(
		@ApplicationContext context: Context
	): ConnectivityManager = ConnectivityManager(context)

	@Provides
	@Singleton
	fun provideNotificationManager(
		@ApplicationContext context: Context
	): NotificationManager = NotificationManager(context)

	@Provides
	@Singleton
	fun provideEncryptionManager(): EncryptionManager = EncryptionManager()

	@Provides
	@Singleton
	fun provideSharedUiEvent(): SharedUiEvent = SharedUiEvent()

	@Provides
	@Singleton
	fun provideSharedData(): SharedData = SharedData()

}