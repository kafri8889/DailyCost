package com.dcns.dailycost.ui.dashboard

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.DestinationArgument
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestination
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.data.TransactionType
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.NoRippleTheme
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.dcns.dailycost.foundation.uicomponent.BalanceCard
import com.dcns.dailycost.foundation.uicomponent.TransactionItem
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
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
            FloatingActionButton(
                shape = CircleShape,
                containerColor = DailyCostTheme.colorScheme.primary,
                onClick = {
                    navigationActions.navigateTo(
                        destination = TopLevelDestinations.Home.note,
                        builder = NavigationActions.defaultNavOptionsBuilder(
                            popTo = TopLevelDestinations.Home.dashboard,
                            inclusivePopUpTo = true
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
        DashboardScreenContent(
            state = state,
            lazyListState = lazyListState,
            onNavigationIconClicked = onNavigationIconClicked,
            onRefresh = {
                viewModel.onAction(DashboardAction.Refresh)
            },
            onNavigateTo = { dest ->
                Timber.i("transaction: ${dest.route}")
                navigationActions.navigateTo(
                    destination = dest,
                    builder = NavigationActions.defaultNavOptionsBuilder(
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
                    onNavigationIconClicked = onNavigationIconClicked,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                )
            }

            item {
                BalanceCard(
                    balance = state.balance,
                    onTopUpClicked = {

                    },
                    modifier = Modifier
                        .fillMaxWidth(0.96f)
                )
            }

            item { 
                TitleSection(
                    title = stringResource(id = R.string.recently_activity),
                    onSeeAllClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                )
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
                        .fillMaxWidth(0.92f)
                )
            }

            items(
                items = state.expenses,
                key = { item -> item.id }
            ) { expense ->
                CompositionLocalProvider(
                    LocalRippleTheme provides NoRippleTheme
                ) {
                    TransactionItem(
                        transaction = expense,
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .animateItemPlacement(tween(256))
                    )
                }
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
                        .fillMaxWidth(0.92f)
                )
            }

            items(
                items = state.incomes,
                key = { item -> item.id }
            ) { income ->
                CompositionLocalProvider(
                    LocalRippleTheme provides NoRippleTheme
                ) {
                    TransactionItem(
                        transaction = income,
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .animateItemPlacement(tween(256))
                    )
                }
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
                    color = DailyCostTheme.colorScheme.labelText
                )
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right_new),
                contentDescription = null,
                tint = DailyCostTheme.colorScheme.labelText,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopAppBar(
    modifier: Modifier = Modifier,
    onNavigationIconClicked: () -> Unit
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
        }
    )
}
