package com.dcns.dailycost.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.dcns.dailycost.ui.app.DailyCostAppViewModel

/**
 * Splash screen digunakan saat aplikasi menunggu hasil mengambil user credential di [DailyCostAppViewModel]
 */
@Composable
fun SplashScreen() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("splash")
    }
}