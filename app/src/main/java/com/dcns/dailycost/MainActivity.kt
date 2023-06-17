package com.dcns.dailycost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.dcns.dailycost.foundation.common.ConnectivityManager
import com.dcns.dailycost.ui.app.DailyCostApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DailyCostApp()
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
