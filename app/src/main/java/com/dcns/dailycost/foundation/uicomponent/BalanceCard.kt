package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.R
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.foundation.common.CurrencyFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.common.primarySystemLocale
import com.dcns.dailycost.foundation.extension.drawFadedEdge
import com.dcns.dailycost.foundation.extension.toWallet
import com.dcns.dailycost.theme.DailyCostTheme
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.WormIndicatorType

@Preview(showBackground = true)
@Composable
private fun BalanceCardPreview() {
    DailyCostTheme {
        BalanceCard(
            balance = UserBalance(
                cash = 90_000.0,
                eWallet = 0.0,
                bankAccount = 1_000_000_000_000_000_000_000_000.0
            ),
            onTopUpClicked = {}
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BalanceCard(
    balance: UserBalance,
    modifier: Modifier = Modifier,
    onTopUpClicked: () -> Unit
) {

    val currency = LocalCurrency.current

    val totalBalance = remember(balance) {
        CurrencyFormatter.format(
            locale = primarySystemLocale,
            amount = balance.cash + balance.eWallet + balance.bankAccount,
            countryCode = currency.countryCode
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.total_balance),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    AnimatedTextByChar(
                        text = totalBalance,
                        style = MaterialTheme.typography.titleLarge,
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
                
                FilledTonalButton(onClick = onTopUpClicked) {
                    Text(stringResource(id = R.string.top_up))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BalancePager(
                balance = balance,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BalancePager(
    balance: UserBalance,
    modifier: Modifier = Modifier
) {

    val state = rememberPagerState { 3 }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        HorizontalPager(
            state = state,
            pageSpacing = 8.dp
        ) { page ->
            when (page) {
                0 -> WalletItem(balance.toWallet(WalletType.Cash))
                1 -> WalletItem(balance.toWallet(WalletType.EWallet))
                2 -> WalletItem(balance.toWallet(WalletType.BankAccount))
            }
        }

        DotsIndicator(
            dotCount = 3,
            pagerState = state,
            type = WormIndicatorType(
                dotsGraphic = DotGraphic(
                    size = 8.dp,
                    borderWidth = 1.dp,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    color = Color.Transparent,
                ),
                wormDotGraphic = DotGraphic(
                    size = 8.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            )
        )
    }
}
