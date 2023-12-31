package com.dcns.dailycost

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Consumer
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.DailyCostBiometricManager
import com.dcns.dailycost.foundation.extension.enqueue
import com.dcns.dailycost.foundation.localized.LocalizedActivity
import com.dcns.dailycost.foundation.worker.Workers
import com.dcns.dailycost.ui.app.DailyCostApp
import com.dcns.dailycost.ui.app.DailyCostAppAction
import com.dcns.dailycost.ui.app.DailyCostAppUiEvent
import com.dcns.dailycost.ui.app.DailyCostAppViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: LocalizedActivity() {

	@Inject
	lateinit var connectivityManager: ConnectivityManager

	@Inject
	lateinit var userCredentialUseCases: UserCredentialUseCases

	private val dailyCostAppViewModel: DailyCostAppViewModel by viewModels()

	private lateinit var biometricManager: DailyCostBiometricManager

	@OptIn(ExperimentalFoundationApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Install splash screen
		installSplashScreen().apply {
			setKeepOnScreenCondition { dailyCostAppViewModel.state.value.userCredential == null }
		}

		// Disable crashlytics in debug mode
		FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

		if (BuildConfig.DEBUG) {
			FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
				val token = task.result
				Timber.i("tongken: $token")
			}
		}

		biometricManager = DailyCostBiometricManager(this).apply {
			setListener(object: DailyCostBiometricManager.BiometricListener {
				override fun onSuccess(result: BiometricPrompt.AuthenticationResult) {
					dailyCostAppViewModel.onAction(DailyCostAppAction.IsBiometricAuthenticated(true))
					Timber.i("Biometric auth: success :D")
				}

				override fun onError(errorCode: Int, errString: CharSequence) {
					Timber.i("Biometric auth: error $errorCode | $errString")

					when (errorCode) {
						// Kalo user nge cancel, keluar aplikasi
						BiometricPrompt.ERROR_NEGATIVE_BUTTON, BiometricPrompt.ERROR_USER_CANCELED -> {
							this@MainActivity.finishAffinity()
						}
					}
				}

				override fun onFailed() {
					Timber.i("Biometric auth: failed! :(")
				}
			})
		}

		WindowCompat.setDecorFitsSystemWindows(window, false)

		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				// If user has logged in, sync data
				if (userCredentialUseCases.getUserCredentialUseCase().firstOrNull()?.isLoggedIn == true) {
					Workers.syncWorker().enqueue(this@MainActivity)
				}
			}
		}

		setContent {
			CompositionLocalProvider(
				LocalOverscrollConfiguration provides if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) null
				else LocalOverscrollConfiguration.current
			) {
				// Handle hot start (app running) deeplink
				DisposableEffect(Unit) {
					val listener = Consumer<Intent> { intent ->
						handleDeepLink(intent)
					}

					addOnNewIntentListener(listener)
					onDispose { removeOnNewIntentListener(listener) }
				}

				DailyCostApp(
					viewModel = dailyCostAppViewModel,
					biometricManager = biometricManager
				)
			}
		}
	}

	/**
	 * Function yg diggunakan untuk menghandle deeplink
	 */
	private fun handleDeepLink(intent: Intent) = lifecycleScope.launch {
		userCredentialUseCases.getUserCredentialUseCase().firstOrNull()?.isLoggedIn?.let { hasLogin ->
			// Cek apakah user sudah login, jika belom jangan di handle
			// nanti otomatis pindah ke login screen
			if (hasLogin) {
				intent.data?.let { uri ->
					dailyCostAppViewModel.sendEvent(DailyCostAppUiEvent.HandleDeepLink(uri))
					dailyCostAppViewModel.onAction(DailyCostAppAction.CanNavigate(false))
				}
			}
		}
	}

	override fun onRestart() {
		super.onRestart()

		// Send event to top level app
		setOnLocaleChangedListener { dailyCostAppViewModel.sendEvent(DailyCostAppUiEvent.LanguageChanged) }
	}

	override fun onStart() {
		super.onStart()

		connectivityManager.registerConnectionObserver(this)

		// Handle cold start (app not running) deeplink
		intent?.let { handleDeepLink(it) }
	}

	override fun onStop() {
		super.onStop()

		removeOnLocaleChangedListener()
		connectivityManager.unregisterConnectionObserver(this)
	}
}
