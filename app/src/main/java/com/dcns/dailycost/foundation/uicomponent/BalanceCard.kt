package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
			onAddWalletClicked = {},
			onMoreClicked = {},
			initialBalanceVisibility = true,
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
	initialBalanceVisibility: Boolean,
	modifier: Modifier = Modifier,
	onTopUpClicked: () -> Unit,
	onAddWalletClicked: () -> Unit,
	onMoreClicked: () -> Unit
) {

	val pagerState = rememberPagerState { WalletType.entries.size }

	val balanceVisibility = remember {
		mutableStateListOf<Boolean>().apply {
			for (i in WalletType.entries.indices) add(initialBalanceVisibility)
		}
	}

	Card(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = DailyCostTheme.colorScheme.primaryContainer.copy(alpha = 0.48f)
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
						walletType = WalletType.Cash,
						showBalance = true,
						onVisibilityChanged = {}
					)
				}
			) { (_, height) ->
				VerticalPager(
					state = pagerState,
					pageSpacing = 8.dp,
					modifier = Modifier
						// Set pager height sesuai ukuran dari [PagerItem]
						.height(height + 8.dp)
				) { page ->
					Box {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.height(height + 8.dp)
								.clip(RoundedCornerShape(12))
								.background(Color(0xff9747ff))
						)

						PagerItem(
							amount = when (WalletType.entries[page]) {
								WalletType.Cash -> balance.cash
								WalletType.EWallet -> balance.eWallet
								WalletType.BankAccount -> balance.bankAccount
							},
							monthlyExpense = 0.0,
							walletType = WalletType.entries[page],
							showBalance = balanceVisibility[page],
							onVisibilityChanged = { visible ->
								balanceVisibility[page] = visible
							}
						)
					}
				}
			}

			Box(
				modifier = Modifier
					.fillMaxWidth()
			) {
				BalanceActionButton(
					icon = painterResource(id = R.drawable.ic_wallet_add),
					title = stringResource(id = R.string.add_wallet),
					onClick = onAddWalletClicked,
					modifier = Modifier
						.padding(8.dp)
						.align(Alignment.CenterStart)
				)

				BalanceActionButton(
					icon = painterResource(id = R.drawable.ic_money_send),
					title = stringResource(id = R.string.top_up),
					onClick = onTopUpClicked,
					modifier = Modifier
						.padding(8.dp)
						.align(Alignment.Center)
				)

				BalanceActionButton(
					icon = painterResource(id = R.drawable.ic_more),
					title = stringResource(id = R.string.more),
					onClick = onMoreClicked,
					modifier = Modifier
						.padding(8.dp)
						.align(Alignment.CenterEnd)
				)
			}
		}
	}
}

@Composable
private fun BalanceActionButton(
	icon: Painter,
	title: String,
	modifier: Modifier = Modifier,
	shape: Shape = RoundedCornerShape(25),
	onClick: () -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.clip(shape)
			.clickable { onClick() }
			.then(modifier)
	) {
		Icon(
			painter = icon,
			contentDescription = null
		)

		Spacer(modifier = Modifier.width(8.dp))

		Text(
			text = title,
			style = MaterialTheme.typography.bodySmall
		)
	}
}

@Composable
private fun PagerItem(
	amount: Double,
	monthlyExpense: Double,
	walletType: WalletType,
	showBalance: Boolean,
	modifier: Modifier = Modifier,
	onVisibilityChanged: (Boolean) -> Unit
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

	CompositionLocalProvider(
		LocalContentColor provides MaterialTheme.colorScheme.onPrimary
	) {
		Card(
			modifier = modifier,
			colors = CardDefaults.cardColors(
				containerColor = Color(0xffFF6600)
			)
		) {
			Box {
				Column(
					verticalArrangement = Arrangement.spacedBy(4.dp),
					modifier = Modifier
						.matchParentSize()
				) {
					for (i in 0 until 30) {
						Icon(
							painter = painterResource(id = R.drawable.wave),
							contentDescription = null,
							tint = Color(0xffC8C7CA),
							modifier = Modifier
								.fillMaxWidth()
								.scale(scaleX = 1.4f, scaleY = 1f)
						)
					}
				}

				Column(
					verticalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.padding(16.dp)
						.zIndex(1f)
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
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.spacedBy(8.dp),
						modifier = Modifier
							.fillMaxWidth()
					) {
						Text(
							text = buildString {
								if (showBalance) append(formattedAmount)
								else {
									var s = "Rp "
									for (i in formattedAmount.indices) s += "•"
									append(s)
								}
							},
							style = MaterialTheme.typography.titleLarge.copy(
								fontWeight = FontWeight.SemiBold
							),
							modifier = Modifier
								.weight(1f)
								.dailyCostMarquee()
						)

						IconButton(
							onClick = {
								onVisibilityChanged(!showBalance)
							}
						) {
							AnimatedContent(
								label = "show_balance",
								targetState = showBalance,
								transitionSpec = {
									fadeIn(
										tween(256)
									) togetherWith fadeOut(
										tween(256)
									)
								}
							) { show ->
								Icon(
									painter = painterResource(id = if (show) R.drawable.ic_eye else R.drawable.ic_eye_slash),
									contentDescription = null
								)
							}
						}
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
	}
}
