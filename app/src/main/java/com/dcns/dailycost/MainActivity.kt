package com.dcns.dailycost

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.foundation.localized.LocalizedActivity
import com.dcns.dailycost.foundation.localized.data.OnLocaleChangedListener
import com.dcns.dailycost.ui.app.DailyCostApp
import com.dcns.dailycost.ui.app.DailyCostAppUiEvent
import com.dcns.dailycost.ui.app.DailyCostAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: LocalizedActivity() {

    @Inject lateinit var connectivityManager: ConnectivityManager

    private val dailyCostAppViewModel: DailyCostAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setListener(object : OnLocaleChangedListener {
            override fun onChanged() {
                dailyCostAppViewModel.sendEvent(DailyCostAppUiEvent.LanguageChanged)
            }
        })

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
