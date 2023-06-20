package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.model.Wallet
import com.dcns.dailycost.foundation.common.CurrencyFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.common.primarySystemLocale
import com.dcns.dailycost.foundation.extension.drawFadedEdge

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletItem(
    wallet: Wallet,
    modifier: Modifier = Modifier
) {

    val currency = LocalCurrency.current

    val balance = remember(wallet.amount) {
        CurrencyFormatter.format(
            locale = primarySystemLocale,
            amount = wallet.amount,
            countryCode = currency.countryCode
        )
    }

    Card(
        modifier = modifier
    ) {
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

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = wallet.walletType.localizedName,
                    style = MaterialTheme.typography.bodySmall
                )

                AnimatedTextByChar(
                    text = balance,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        // Rendering to an offscreen buffer is required to get the faded edges' alpha to be
                        // applied only to the text, and not whatever is drawn below this composable (e.g. the
                        // window).
                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                        .drawWithContent {
                            drawContent()
                            drawFadedEdge(
                                edgeWidth = 8.dp,
                                leftEdge = false
                            )
                        }
                        .basicMarquee(
                            delayMillis = 2000
                        )
                )
            }
        }
    }
}
