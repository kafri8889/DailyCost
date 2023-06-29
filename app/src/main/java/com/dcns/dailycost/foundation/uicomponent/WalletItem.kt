package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.model.Wallet
import com.dcns.dailycost.foundation.common.CurrencyFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.common.primarySystemLocale
import com.dcns.dailycost.foundation.extension.dailyCostMarquee

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectableWalletItem(
    wallet: Wallet,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val scaleAnimatable = remember { Animatable(1f) }

    LaunchedEffect(selected) {
        if (selected) {
            scaleAnimatable.animateTo(
                targetValue = 0.88f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            )

            scaleAnimatable.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy
                )
            )
        }
    }

    Card(
        onClick = onClick,
        border = if (selected) BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ) else null,
        modifier = modifier
            .graphicsLayer {
                scaleX = scaleAnimatable.value
                scaleY = scaleAnimatable.value
            }
    ) {
        WalletItemContent(
            wallet = wallet,
            trailingIcon = {
                RadioButton(
                    selected = selected,
                    onClick = onClick
                )
            }
        )
    }
}

@Composable
fun WalletItem(
    wallet: Wallet,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier) {
        WalletItemContent(wallet = wallet)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WalletItemContent(
    wallet: Wallet,
    trailingIcon: @Composable () -> Unit = {}
) {

    val currency = LocalCurrency.current

    val balance = remember(wallet.amount) {
        CurrencyFormatter.format(
            locale = primarySystemLocale,
            amount = wallet.amount,
            countryCode = currency.countryCode
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(wallet.walletType.icon),
            contentDescription = null
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = wallet.walletType.localizedName,
                style = MaterialTheme.typography.bodySmall
            )

            AnimatedTextByChar(
                text = balance,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .dailyCostMarquee()
            )
        }

        trailingIcon()
    }
}
