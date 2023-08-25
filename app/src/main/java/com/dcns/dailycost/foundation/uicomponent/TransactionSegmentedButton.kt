package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.R
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Composable
fun TransactionSegmentedButton(
	selectedTransactionType: TransactionType,
	modifier: Modifier = Modifier,
	onTransactionTypeChanged: (TransactionType) -> Unit
) {

	Box(
		contentAlignment = Alignment.Center,
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.padding(8.dp)
		) {
			Button(
				shape = RoundedCornerShape(25),
				onClick = {
					onTransactionTypeChanged(TransactionType.Income)
				},
				colors = ButtonDefaults.buttonColors(
					containerColor = if (selectedTransactionType == TransactionType.Income) DailyCostTheme.colorScheme.primary else Color.Transparent
				),
				modifier = Modifier
					.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.income),
					style = LocalTextStyle.current.copy(
						color = if (selectedTransactionType == TransactionType.Income) DailyCostTheme.colorScheme.onPrimary else DailyCostTheme.colorScheme.text
					)
				)
			}

			Button(
				shape = RoundedCornerShape(25),
				onClick = {
					onTransactionTypeChanged(TransactionType.Expense)
				},
				colors = ButtonDefaults.buttonColors(
					containerColor = if (selectedTransactionType == TransactionType.Expense) DailyCostTheme.colorScheme.primary else Color.Transparent
				),
				modifier = Modifier
					.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.expenses),
					style = LocalTextStyle.current.copy(
						color = if (selectedTransactionType == TransactionType.Expense) DailyCostTheme.colorScheme.onPrimary else DailyCostTheme.colorScheme.text
					)
				)
			}
		}
	}
}
