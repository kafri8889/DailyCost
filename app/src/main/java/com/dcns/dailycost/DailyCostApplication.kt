package com.dcns.dailycost

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DailyCostApplication: Application(), Configuration.Provider {

	@Inject
	lateinit var workerFactory: HiltWorkerFactory

	override fun onCreate() {
		super.onCreate()

		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationUtil.createChannel(this)
//        }

		WorkManager.initialize(this, workManagerConfiguration)
	}

	override fun getWorkManagerConfiguration() =
		Configuration.Builder()
			.setWorkerFactory(workerFactory)
			.build()
}