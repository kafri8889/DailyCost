package com.dcns.dailycost

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkManager
import com.dcns.dailycost.domain.repository.IUserCredentialRepository
import com.dcns.dailycost.domain.use_case.BalanceUseCases
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.common.Workers
import com.dcns.dailycost.foundation.extension.enqueue
import com.dcns.dailycost.foundation.localized.LocalizedActivity
import com.dcns.dailycost.foundation.localized.data.OnLocaleChangedListener
import com.dcns.dailycost.ui.app.DailyCostApp
import com.dcns.dailycost.ui.app.DailyCostAppUiEvent
import com.dcns.dailycost.ui.app.DailyCostAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: LocalizedActivity() {

    @Inject lateinit var connectivityManager: ConnectivityManager
    @Inject lateinit var userCredentialRepository: IUserCredentialRepository
    @Inject lateinit var balanceUseCases: BalanceUseCases

    private val dailyCostAppViewModel: DailyCostAppViewModel by viewModels()

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(this@MainActivity)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setListener(object : OnLocaleChangedListener {
            override fun onChanged() {
                dailyCostAppViewModel.sendEvent(DailyCostAppUiEvent.LanguageChanged)
            }
        })

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Workers.syncWorker().enqueue(this@MainActivity)
            }
        }

        setContent {
            DailyCostApp(
                viewModel = dailyCostAppViewModel
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
