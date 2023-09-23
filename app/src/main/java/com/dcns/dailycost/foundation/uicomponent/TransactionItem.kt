package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
	modifier: Modifier = Modifier,
	shape: Shape = RoundedCornerShape(0),
	contentPadding: PaddingValues = PaddingValues(0.dp),
	containerColor: Color = MaterialTheme.colorScheme.background
) {

	val context = LocalContext.current

	Card(
		modifier = modifier,
		shape = shape,
		colors = CardDefaults.cardColors(
			containerColor = containerColor
		)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.padding(contentPadding)
				.fillMaxWidth()
				.padding(
					vertical = 8.dp
				)
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.clip(CircleShape)
					.size(36.dp)
					.background(Color(transaction.category.colorArgb))
			) {
				Icon(
					painter = painterResource(id = transaction.category.icon.iconResId),
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onPrimary
				)
			}

			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
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
								edgeWidth = 0.dp
							)
					)

					Spacer(modifier = Modifier.width(8.dp))

					Text(
						text = CommonDateFormatter.edmy(context.primaryLocale)
							.format(transaction.date),
						style = MaterialTheme.typography.labelSmall.copy(
							color = DailyCostTheme.colorScheme.labelText
						)
					)
				}

				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.fillMaxWidth()
				) {
					Text(
						text = transaction.category.name,
						style = MaterialTheme.typography.bodyMedium.copy(
							fontWeight = FontWeight.Normal,
							color = DailyCostTheme.colorScheme.labelText
						),
						modifier = Modifier
							.weight(0.5f)
					)

					Text(
						text = "${if (transaction.isExpense) "" else "+"}${transaction.parseAmount(LocalCurrency.current.countryCode)}",
						textAlign = TextAlign.End,
						style = MaterialTheme.typography.titleSmall.copy(
							fontWeight = FontWeight.SemiBold,
							color = if (transaction.isExpense) DailyCostTheme.colorScheme.expense else DailyCostTheme.colorScheme.income
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissableTransactionItem(
	transaction: Transaction,
	modifier: Modifier = Modifier,
	contentPadding: PaddingValues = PaddingValues(0.dp),
	containerColor: Color = MaterialTheme.colorScheme.background,
	onDismiss: () -> Unit
) {
	val dismissState = rememberDismissState(
		confirmValueChange = { dismissValue ->
			if (dismissValue == DismissValue.DismissedToEnd) {
				onDismiss()
				return@rememberDismissState true
			}

			false
		}
	)

	SwipeToDismiss(
		state = dismissState,
		directions = setOf(DismissDirection.StartToEnd),
		background = {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.errorContainer)
			)
		},
		dismissContent = {
			TransactionItem(
				shape = if (dismissState.dismissDirection == DismissDirection.StartToEnd) RoundedCornerShape(25)
				else RoundedCornerShape(0),
				contentPadding = contentPadding,
				containerColor = containerColor,
				transaction = transaction,
				modifier = modifier
			)
		}
	)
}

@Preview(showBackground = true)
@Composable
private fun TransactionCardPreview() {
	DailyCostTheme {
		TransactionItem(
			transaction = LocalExpenseDataProvider.expense1
		)
	}
}
