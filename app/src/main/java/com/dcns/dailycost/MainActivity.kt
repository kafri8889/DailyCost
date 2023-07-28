package com.dcns.dailycost

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dcns.dailycost.domain.use_case.UserCredentialUseCases
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.DailyCostBiometricManager
import com.dcns.dailycost.foundation.extension.enqueue
import com.dcns.dailycost.foundation.localized.LocalizedActivity
import com.dcns.dailycost.foundation.localized.data.OnLocaleChangedListener
import com.dcns.dailycost.foundation.worker.Workers
import com.dcns.dailycost.ui.app.DailyCostApp
import com.dcns.dailycost.ui.app.DailyCostAppAction
import com.dcns.dailycost.ui.app.DailyCostAppUiEvent
import com.dcns.dailycost.ui.app.DailyCostAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: LocalizedActivity() {

    @Inject lateinit var connectivityManager: ConnectivityManager
    @Inject lateinit var userCredentialUseCases: UserCredentialUseCases

    private val dailyCostAppViewModel: DailyCostAppViewModel by viewModels()

    private lateinit var biometricManager: DailyCostBiometricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        biometricManager = DailyCostBiometricManager(this).apply {
            setListener(object : DailyCostBiometricManager.BiometricListener {
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

        connectivityManager.initialize()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setListener(object : OnLocaleChangedListener {
            override fun onChanged() {
                dailyCostAppViewModel.sendEvent(DailyCostAppUiEvent.LanguageChanged)
            }
        })

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (userCredentialUseCases.getUserCredentialUseCase().firstOrNull()?.isLoggedIn == true) {
                    Workers.syncWorker().enqueue(this@MainActivity)
                }
            }
        }

        setContent {
            DailyCostApp(
                viewModel = dailyCostAppViewModel,
                biometricManager = biometricManager
            )
        }
    }

    override fun onStart() {
        super.onStart()

        connectivityManager.registerConnectionObserver(this)
    }

    override fun onStop() {
        super.onStop()

        connectivityManager.unregisterConnectionObserver(this)
    }
}
