package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.R
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.UserBalance
import com.dcns.dailycost.foundation.common.CurrencyFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.common.primarySystemLocale
import com.dcns.dailycost.foundation.extension.dailyCostMarquee
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Preview(showBackground = true)
@Composable
private fun BalanceCardPreview() {
	DailyCostTheme {
		BalanceCard(
			onTopUpClicked = {},
			balance = UserBalance(
				cash = 90_000.0,
				eWallet = 0.0,
				bankAccount = 1_000_000_000_000_000_000_000_000.0
			)
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

	val pagerState = rememberPagerState { 3 }

	Card(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = Color(0xff7E7E7E)
		)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.padding(8.dp)
		) {
			Measurer(
				contentToMeasure = {
					// Ukur size untuk PagerItem
					PagerItem(
						amount = balance.cash,
						monthlyExpense = 0.0,
						walletType = WalletType.Cash
					)
				}
			) { (_, height) ->
				VerticalPager(
					state = pagerState,
					pageSpacing = 8.dp,
					modifier = Modifier
						// Set pager height sesuai ukuran dari [PagerItem]
						.height(height)
				) { page ->
					when (page) {
						0 -> PagerItem(
							amount = balance.cash,
							monthlyExpense = 0.0,
							walletType = WalletType.Cash
						)

						1 -> PagerItem(
							amount = balance.eWallet,
							monthlyExpense = 0.0,
							walletType = WalletType.EWallet
						)

						2 -> PagerItem(
							amount = balance.bankAccount,
							monthlyExpense = 0.0,
							walletType = WalletType.BankAccount
						)
					}
				}
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.clip(RoundedCornerShape(25))
						.clickable { onTopUpClicked() }
						.padding(4.dp)
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_money_send),
						contentDescription = null
					)

					Spacer(modifier = Modifier.width(8.dp))

					Text(
						text = stringResource(id = R.string.top_up),
						style = MaterialTheme.typography.bodySmall
					)
				}
			}
		}
	}
}

@Composable
private fun PagerItem(
	amount: Double,
	monthlyExpense: Double,
	walletType: WalletType,
	modifier: Modifier = Modifier
) {
	val currency = LocalCurrency.current

	val formattedAmount = remember(amount) {
		CurrencyFormatter.format(
			locale = primarySystemLocale,
			amount = amount,
			countryCode = currency.countryCode
		)
	}

	val formattedMonthlyExpense = remember(monthlyExpense) {
		CurrencyFormatter.format(
			locale = primarySystemLocale,
			amount = monthlyExpense,
			countryCode = currency.countryCode
		)
	}

	Card(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = Color(0xffAEAEAE)
		)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.padding(16.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_empty_wallet),
					contentDescription = null,
					modifier = Modifier
						.size(16.dp)
				)

				Text(
					text = walletType.localizedName,
					style = MaterialTheme.typography.bodyMedium
				)
			}

			Row(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.fillMaxWidth()
			) {
				Text(
					text = formattedAmount,
					style = MaterialTheme.typography.titleLarge.copy(
						fontWeight = FontWeight.SemiBold
					),
					modifier = Modifier
						.weight(1f)
						.dailyCostMarquee()
				)

				Icon(
					painter = painterResource(id = R.drawable.ic_eye),
					contentDescription = null
				)
			}

			Text(
				text = stringResource(
					id = R.string.monthly_expenses_x,
					formattedMonthlyExpense
				),
				modifier = Modifier
					.fillMaxWidth()
					.dailyCostMarquee()
			)
		}
	}
}
