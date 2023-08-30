package com.dcns.dailycost.ui.transactions

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.TransactionItem

@Composable
fun TransactionsScreen(
	viewModel: TransactionsViewModel,
	navigationActions: NavigationActions
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

	BaseScreenWrapper(
		viewModel = viewModel,
		floatingActionButton = {
			FloatingActionButton(
				shape = CircleShape,
				containerColor = DailyCostTheme.colorScheme.primary,
				onClick = {
					navigationActions.navigateTo(
						destination = TopLevelDestinations.Home.transaction.createRoute(
							DestinationArgument.TRANSACTION_ID to -1,
							DestinationArgument.ACTION_MODE to ActionMode.New,
							DestinationArgument.TRANSACTION_TYPE to (state.selectedTransactionType
								?: TransactionType.Income)
						)
					)
				}
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_add),
					contentDescription = null,
					tint = DailyCostTheme.colorScheme.onPrimary
				)
			}
		}
	) { _ ->
		TransactionsScreenContent(
			state = state,
			onNavigationIconClicked = navigationActions::popBackStack,
			onNavigateTo = { dest ->
				navigationActions.navigateTo(dest)
			},
			modifier = Modifier
				.statusBarsPadding()
		)
	}
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TransactionsScreenContent(
	state: TransactionsState,
	modifier: Modifier = Modifier,
	onNavigateTo: (TopLevelDestination) -> Unit,
	onNavigationIconClicked: () -> Unit
) {

	LazyColumn(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
	) {
		item {
			TopAppBar(
				title = {
					Text(
						stringResource(
							when (state.selectedTransactionType) {
								TransactionType.Expense -> R.string.expenses
								TransactionType.Income -> R.string.income
								else -> R.string.transactions
							}
						)
					)
				},
				navigationIcon = {
					IconButton(onClick = onNavigationIconClicked) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				}
			)
		}

		items(
			items = state.transactions,
			key = { item -> item.id }
		) { transaction ->
			TransactionItem(
				transaction = transaction,
				modifier = Modifier
					.clickable {
						onNavigateTo(
							TopLevelDestinations.Home.transaction.createRoute(
								DestinationArgument.TRANSACTION_ID to transaction.id,
								DestinationArgument.TRANSACTION_TYPE to if (transaction.isIncome) TransactionType.Income else TransactionType.Expense,
								DestinationArgument.ACTION_MODE to ActionMode.View,
							)
						)
					}
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.animateItemPlacement(tween(256))
			)
		}

		item {
			// Fab size: 56.dp
			Spacer(modifier = Modifier.height(56.dp + 16.dp))
		}
	}
}
