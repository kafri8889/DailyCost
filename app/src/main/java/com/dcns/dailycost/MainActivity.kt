package com.dcns.dailycost

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.dcns.dailycost.data.model.remote.request_body.DepoRequestBody
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: LocalizedActivity() {

    @Inject lateinit var connectivityManager: ConnectivityManager
    @Inject lateinit var userCredentialRepository: IUserCredentialRepository
    @Inject lateinit var balanceUseCases: BalanceUseCases

    private val dailyCostAppViewModel: DailyCostAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setListener(object : OnLocaleChangedListener {
            override fun onChanged() {
                dailyCostAppViewModel.sendEvent(DailyCostAppUiEvent.LanguageChanged)
            }
        })

        lifecycleScope.launch {
            connectivityManager.isNetworkAvailable.asFlow().collect { have ->
                val credential = userCredentialRepository.getUserCredential.firstOrNull()
                val balance = balanceUseCases.getLocalBalanceUseCase().firstOrNull()

                if (credential == null || balance == null) return@collect

                Workers.uploadBalanceWorker(
                    body = DepoRequestBody(
                        id = credential.id.toInt(),
                        cash = balance.cash.toInt(),
                        eWallet = balance.eWallet.toInt(),
                        bankAccount = balance.bankAccount.toInt()
                    )
                ).enqueue(this@MainActivity)
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
