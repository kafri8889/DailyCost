package com.dcns.dailycost.ui.notification

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.uicomponent.NotificationItem

@Composable
fun NotificationScreen(
	viewModel: NotificationViewModel,
	navigationActions: NavigationActions
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

	BaseScreenWrapper(
		viewModel = viewModel
	) { scaffoldPadding ->
		NotificationScreenContent(
			state = state,
			onNavigationIconClicked = navigationActions::popBackStack,
			onNotificationClicked = { notification ->
				viewModel.onAction(
					NotificationAction.UpdateNotification(
						notification.copy(
							hasBeenRead = true
						)
					)
				)
			},
			modifier = Modifier
				.fillMaxSize()
				.statusBarsPadding()
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun NotificationScreenContent(
	state: NotificationState,
	modifier: Modifier = Modifier,
	onNotificationClicked: (Notification) -> Unit,
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
					Text(stringResource(id = R.string.notification))
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
			items = state.notifications,
			key = { item -> item.id }
		) { notification ->
			NotificationItem(
				notification = notification,
				onClick = {
					onNotificationClicked(notification)
				},
				modifier = Modifier
					.fillMaxWidth(0.92f)
					.animateItemPlacement(tween(256))
			)
		}
	}
}
