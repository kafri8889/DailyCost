package com.dcns.dailycost.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.ActionMode
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.data.defaultNavOptionsBuilder
import com.dcns.dailycost.data.model.Expense
import com.dcns.dailycost.data.model.Income
import com.dcns.dailycost.data.model.Note
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.extension.toast
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.BalanceCard
import com.dcns.dailycost.foundation.uicomponent.Measurer
import com.dcns.dailycost.foundation.uicomponent.TransactionItem
import timber.log.Timber

@Composable
fun DashboardScreen(
	viewModel: DashboardViewModel,
	navigationActions: NavigationActions,
	onNavigationIconClicked: () -> Unit
) {

	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val lazyListState = rememberLazyListState()

	BaseScreenWrapper(
		viewModel = viewModel,
		floatingActionButton = {
			DashboardFloatingActionButton(
				navigateTo = { dest ->
					if (dest.route == TopLevelDestinations.Home.note.route) {
						"Fitur belom tersedia (>‿◠)✌".toast(context)
						return@DashboardFloatingActionButton
					}
					navigationActions.navigateTo(
						destination = dest,
						builder = defaultNavOptionsBuilder(
							popTo = TopLevelDestinations.Home.dashboard,
							inclusivePopUpTo = false
						)
					)
				}
			)
		}
	) { _ ->
		DashboardScreenContent(
			state = state,
			lazyListState = lazyListState,
			onNavigationIconClicked = onNavigationIconClicked,
			onRefresh = {
				viewModel.onAction(DashboardAction.Refresh(context))
			},
			onNavigateTo = { dest ->
				Timber.i("transaction: ${dest.route}")
				navigationActions.navigateTo(
					destination = dest,
					builder = defaultNavOptionsBuilder(
						popTo = TopLevelDestinations.Home.dashboard
					)
				)
			},
			modifier = Modifier
				.statusBarsPadding()
		)
	}
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun DashboardScreenContent(
	state: DashboardState,
	lazyListState: LazyListState,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit,
	onNavigateTo: (TopLevelDestination) -> Unit,
	onRefresh: () -> Unit
) {

	val context = LocalContext.current

	val pullRefreshState = rememberPullRefreshState(
		refreshing = state.isRefreshing,
		onRefresh = onRefresh
	)

	Box(
		modifier = modifier
			.pullRefresh(pullRefreshState)
	) {
		LazyColumn(
			state = lazyListState,
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.fillMaxSize()
				.pullRefresh(pullRefreshState)
		) {
			item {
				DashboardTopAppBar(
					unreadNotificationCount = state.unreadNotificationCount,
					onNavigationIconClicked = onNavigationIconClicked,
					onNotificationIconClicked = {
						onNavigateTo(TopLevelDestinations.Home.notification)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				)
			}

			item {
				BalanceCard(
					balance = state.balances,
					initialBalanceVisibility = state.initialBalanceVisibility,
					onAddWalletClicked = {
						"Fitur belom tersedia (>‿◠)✌".toast(context)
					},
					onTopUpClicked = {
						"Fitur belom tersedia (>‿◠)✌".toast(context)
					},
					onMoreClicked = {
						"Fitur belom tersedia (>‿◠)✌".toast(context)
					},
					modifier = Modifier
						.fillMaxWidth(0.96f)
				)
			}

			item {
				TitleSection(
					title = stringResource(id = R.string.recently_activity),
					onSeeAllClick = {
						onNavigateTo(TopLevelDestinations.Home.recentActivity)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				)
			}

			items(
				items = state.recentActivity,
				key = { item ->
					when (item) {
						is Income -> item.id + item.hashCode()
						is Expense -> item.id + item.hashCode()
						is Note -> item.id + item.hashCode()
						else -> item.hashCode()
					}
				}
			) { any ->
				val anyModifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.animateItemPlacement(tween(256))

				when (any) {
					is Expense -> {
						TransactionItem(
							transaction = any,
							modifier = Modifier
								.clickable {
									onNavigateTo(
										TopLevelDestinations.Home.transaction.createRoute(
											DestinationArgument.TRANSACTION_ID to any.id,
											DestinationArgument.TRANSACTION_TYPE to TransactionType.Expense,
											DestinationArgument.ACTION_MODE to ActionMode.View,
										)
									)
								}
								.then(anyModifier)
						)
					}
					is Income -> {
						TransactionItem(
							transaction = any,
							modifier = Modifier
								.clickable {
									onNavigateTo(
										TopLevelDestinations.Home.transaction.createRoute(
											DestinationArgument.TRANSACTION_ID to any.id,
											DestinationArgument.TRANSACTION_TYPE to TransactionType.Income,
											DestinationArgument.ACTION_MODE to ActionMode.View,
										)
									)
								}
								.then(anyModifier)
						)
					}
				}
			}

			item {
				TitleSection(
					title = stringResource(id = R.string.expenses),
					onSeeAllClick = {
						onNavigateTo(
							TopLevelDestinations.Home.transactions.createRoute(
								DestinationArgument.TRANSACTION_TYPE to TransactionType.Expense
							)
						)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				)
			}

			items(
				items = state.expenses,
				key = { item -> item.id }
			) { expense ->
				TransactionItem(
					transaction = expense,
					modifier = Modifier
						.clickable {
							onNavigateTo(
								TopLevelDestinations.Home.transaction.createRoute(
									DestinationArgument.TRANSACTION_ID to expense.id,
									DestinationArgument.TRANSACTION_TYPE to TransactionType.Expense,
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
				TitleSection(
					title = stringResource(id = R.string.income),
					onSeeAllClick = {
						onNavigateTo(
							TopLevelDestinations.Home.transactions.createRoute(
								DestinationArgument.TRANSACTION_TYPE to TransactionType.Income
							)
						)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				)
			}

			items(
				items = state.incomes,
				key = { item -> item.id }
			) { income ->
				TransactionItem(
					transaction = income,
					modifier = Modifier
						.clickable {
							onNavigateTo(
								TopLevelDestinations.Home.transaction.createRoute(
									DestinationArgument.TRANSACTION_ID to income.id,
									DestinationArgument.TRANSACTION_TYPE to TransactionType.Income,
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

		PullRefreshIndicator(
			refreshing = state.isRefreshing,
			state = pullRefreshState,
			modifier = Modifier
				.align(Alignment.TopCenter)
		)
	}
}

@Composable
private fun TitleSection(
	title: String,
	modifier: Modifier = Modifier,
	onSeeAllClick: () -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = modifier
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleMedium.copy(
				fontWeight = FontWeight.Bold
			)
		)

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.clip(RoundedCornerShape(25))
				.clickable { onSeeAllClick() }
				.padding(4.dp)
		) {
			Text(
				text = stringResource(id = R.string.see_all),
				style = MaterialTheme.typography.labelLarge.copy(
					color = DailyCostTheme.colorScheme.text
				)
			)

			Icon(
				painter = painterResource(id = R.drawable.ic_arrow_right_new),
				contentDescription = null,
				tint = DailyCostTheme.colorScheme.text,
				modifier = Modifier
					.size(16.dp)
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopAppBar(
	unreadNotificationCount: Int,
	modifier: Modifier = Modifier,
	onNavigationIconClicked: () -> Unit,
	onNotificationIconClicked: () -> Unit
) {

	TopAppBar(
		modifier = modifier,
		title = {},
		navigationIcon = {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.minimumInteractiveComponentSize()
					.size(40.dp)
					.clip(RoundedCornerShape(40))
					.border(
						width = 1.dp,
						color = MaterialTheme.colorScheme.outline,
						shape = RoundedCornerShape(40)
					)
					.clickable(
						onClick = onNavigationIconClicked,
						role = Role.Button,
						interactionSource = remember { MutableInteractionSource() },
						indication = rememberRipple(
							bounded = false
						)
					)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_categories),
					contentDescription = null
				)
			}
		},
		actions = {
			BadgedBox(
				badge = {
					if (unreadNotificationCount > 0) {
						Badge {
							Text(text = unreadNotificationCount.toString())
						}
					}
				}
			) {
				Box(
					contentAlignment = Alignment.Center,
					modifier = Modifier
						.minimumInteractiveComponentSize()
						.size(40.dp)
						.clip(RoundedCornerShape(40))
						.border(
							width = 1.dp,
							color = MaterialTheme.colorScheme.outline,
							shape = RoundedCornerShape(40)
						)
						.clickable(
							onClick = onNotificationIconClicked,
							role = Role.Button,
							interactionSource = remember { MutableInteractionSource() },
							indication = rememberRipple(
								bounded = false
							)
						)
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_notification_bell),
						contentDescription = null
					)
				}
			}
		}
	)
}

@Composable
private fun DashboardFloatingActionButton(
	modifier: Modifier = Modifier,
	navigateTo: (TopLevelDestination) -> Unit
) {

	var expanded by remember { mutableStateOf(false) }

	Column(
		horizontalAlignment = Alignment.End,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
	) {
		Measurer(
			contentToMeasure = {
				DashboardFloatingActionButtonTooltip(
					text = arrayOf(stringResource(id = R.string.note), stringResource(id = R.string.transaction)).maxBy { it.length },
					tooltipWidth = Dp.Unspecified
				)
			}
		) { (width, _) ->
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
			) {
				DashboardFloatingActionButtonItem(
					icon = R.drawable.ic_book_closed,
					text = stringResource(id = R.string.notes),
					expanded = expanded,
					tooltipWidth = width
				) {
					expanded = false
					navigateTo(TopLevelDestinations.Home.note)
				}

				DashboardFloatingActionButtonItem(
					icon = R.drawable.ic_transaction,
					text = stringResource(id = R.string.transaction),
					expanded = expanded,
					tooltipWidth = width
				) {
					expanded = false
					navigateTo(TopLevelDestinations.Home.transaction)
				}
			}
		}

		FloatingActionButton(
			shape = CircleShape,
			containerColor = DailyCostTheme.colorScheme.primary,
			onClick = {
				expanded = !expanded
			}
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_add),
				contentDescription = null,
				tint = DailyCostTheme.colorScheme.onPrimary,
				modifier = Modifier
					.composed {
						val angle by animateFloatAsState(
							label = "angle",
							targetValue = if (expanded) 315f else 0f
						)

						graphicsLayer {
							rotationZ = angle
						}
					}
			)
		}
	}
}

@Composable
private fun DashboardFloatingActionButtonTooltip(
	text: String,
	tooltipWidth: Dp
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.width(tooltipWidth)
			.clip(RoundedCornerShape(25))
			.background(Color(0xff1A1A1A).copy(alpha = 0.75f))
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.bodySmall.copy(
				fontWeight = FontWeight.Medium,
				color = DailyCostTheme.colorScheme.onPrimary
			),
			modifier = Modifier
				.padding(10.dp)
		)
	}
}

@Composable
private fun DashboardFloatingActionButtonItem(
	icon: Int,
	text: String,
	expanded: Boolean,
	tooltipWidth: Dp,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		modifier = modifier
	) {
		AnimatedVisibility(
			visible = expanded,
			enter = scaleIn(tween(512)),
			exit = scaleOut(tween(512))
		) {
			DashboardFloatingActionButtonTooltip(
				text = text,
				tooltipWidth = tooltipWidth
			)
		}

		AnimatedVisibility(
			visible = expanded,
			enter = scaleIn(tween(512)),
			exit = scaleOut(tween(512))
		) {
			FloatingActionButton(
				shape = CircleShape,
				onClick = onClick,
				containerColor = DailyCostTheme.colorScheme.primary,
				elevation = FloatingActionButtonDefaults.elevation(
					defaultElevation = 0.dp
				)
			) {
				Icon(
					painter = painterResource(id = icon),
					contentDescription = null,
					tint = DailyCostTheme.colorScheme.onPrimary
				)
			}
		}
	}

}
