package com.dcns.dailycost.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.uicomponent.BalanceCard
import com.dcns.dailycost.foundation.uicomponent.NoteItem
import com.dcns.dailycost.theme.DailyCostTheme

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

    val expandFab by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex < 1
        }
    }

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CenterAlignedTopAppBar(
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
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                )
            }
        },
        floatingActionButton = {
            DashboardFloatingActionButton(
                expandFab = expandFab,
                onShoppingClicked = {
                    // TODO: ke add shopping
                },
                onNotesClicked = {
                    navigationActions.navigateTo(
                        destination = TopLevelDestinations.Home.createEditNote,
                        inclusivePopUpTo = true
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    ) { scaffoldPadding ->
        DashboardScreenContent(
            state = state,
            lazyListState = lazyListState,
            onRefresh = {
                viewModel.onAction(DashboardAction.Refresh)
            },
            modifier = Modifier
                .padding(scaffoldPadding)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DashboardScreenContent(
    state: DashboardState,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
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
                BalanceCard(
                    balance = state.balance,
                    modifier = Modifier
                        .fillMaxWidth(0.96f)
                )
            }

            item { 
                AnimatedVisibility(
                    visible = state.recentNotes.isNotEmpty(),
                    enter = expandHorizontally(tween(512)),
                    exit = shrinkHorizontally(tween(512)),
                ) {
                    Text(
                        text = stringResource(id = R.string.recent_notes),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth(0.96f)
                    )
                }
            }

            items(
                items = state.recentNotes,
                key = { note -> note.id }
            ) { note ->
                NoteItem(
                    note = note,
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth(0.96f)
                )
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
private fun DashboardFloatingActionButton(
    expandFab: Boolean,
    modifier: Modifier = Modifier,
    onShoppingClicked: () -> Unit,
    onNotesClicked: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(512)),
            exit = fadeOut(tween(256))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ClickableText(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.note))
                    },
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        onNotesClicked()
                    }
                )

                SmallFloatingActionButton(
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    onClick = onNotesClicked
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_notes),
                        contentDescription = null
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(256)),
            exit = fadeOut(tween(512))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ClickableText(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.shopping))
                    },
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        onShoppingClicked()
                        expanded = false
                    }
                )

                SmallFloatingActionButton(
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                    onClick = {
                        onShoppingClicked()
                        expanded = false
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shopping_bag),
                        contentDescription = null
                    )
                }
            }
        }

        ExtendedFloatingActionButton(
            expanded = expandFab && !expanded,
            text = {
                Text(stringResource(id = R.string.add))
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .composed {
                            val angle by animateFloatAsState(
                                label = "angle",
                                targetValue = if (expanded) 405f else 0f,
                                animationSpec = tween(512)
                            )

                            graphicsLayer {
                                rotationZ = angle
                            }
                        }
                )
            },
            onClick = {
                expanded = !expanded
            }
        )
    }
}

@Preview
@Composable
private fun DashboardFloatingActionButtonPreview(
    @PreviewParameter(BooleanPreviewParameter::class) expanded: Boolean
) {
    DailyCostTheme {
        DashboardFloatingActionButton(
            expandFab = expanded,
            onShoppingClicked = {},
            onNotesClicked = {}
        )
    }
}

private class BooleanPreviewParameter: PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}
