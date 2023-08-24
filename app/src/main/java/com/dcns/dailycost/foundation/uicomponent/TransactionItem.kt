package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.R
import com.dcns.dailycost.data.datasource.local.LocalExpenseDataProvider
import com.dcns.dailycost.foundation.common.CommonDateFormatter
import com.dcns.dailycost.foundation.common.LocalCurrency
import com.dcns.dailycost.foundation.common.Transaction
import com.dcns.dailycost.foundation.extension.dailyCostMarquee
import com.dcns.dailycost.foundation.extension.primaryLocale
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Composable
fun TransactionItem(
	transaction: Transaction,
	modifier: Modifier = Modifier
) {

	val context = LocalContext.current

	Card(
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = Color.Transparent
		)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					vertical = 8.dp
				)
		) {
			Icon(
				painter = painterResource(id = transaction.category.icon.iconResId),
				contentDescription = null,
				modifier = Modifier
					.clip(CircleShape)
			)

			Column(
				verticalArrangement = Arrangement.spacedBy(4.dp),
				modifier = Modifier
					.weight(1f)
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween,
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = transaction.name,
						style = MaterialTheme.typography.titleMedium.copy(
							fontWeight = FontWeight.Medium,
							color = DailyCostTheme.colorScheme.text
						),
						modifier = Modifier
							.weight(1f)
							.dailyCostMarquee(
								edgeWidth = 8.dp
							)
					)

					Spacer(modifier = Modifier.width(8.dp))

					Box(
						contentAlignment = Alignment.Center,
						modifier = Modifier
							.clip(CircleShape)
							.background(MaterialTheme.colorScheme.tertiaryContainer)
					) {
						Text(
							text = stringResource(
								id = if (transaction.isExpense) R.string.expenses
								else R.string.income
							),
							style = MaterialTheme.typography.bodyMedium.copy(
								color = MaterialTheme.colorScheme.tertiary
							),
							modifier = Modifier
								.padding(
									vertical = 4.dp,
									horizontal = 8.dp
								)
						)
					}
				}

				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = CommonDateFormatter.edmy(context.primaryLocale)
							.format(transaction.date),
						style = MaterialTheme.typography.labelSmall.copy(
							fontWeight = FontWeight.Normal,
							color = DailyCostTheme.colorScheme.labelText
						),
						modifier = Modifier
							.weight(0.5f)
					)

					Text(
						text = transaction.parseAmount(LocalCurrency.current.countryCode),
						textAlign = TextAlign.End,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.SemiBold,
							color = DailyCostTheme.colorScheme.text
						),
						modifier = Modifier
							.weight(0.5f)
							.dailyCostMarquee(
								edgeWidth = 0.dp
							)
					)
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun TransactionCardPreview() {
	DailyCostTheme {
		TransactionItem(
			transaction = LocalExpenseDataProvider.expense1.copy(
				name = "Loooooooooooooooooonnnnnnnnnnnnnnnggggggggggg tttteeeeeeekkkkkk"
			)
		)
	}
}
