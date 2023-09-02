package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.datasource.local.LocalNotificationDataProvider
import com.dcns.dailycost.data.model.Notification
import com.dcns.dailycost.foundation.extension.dailyCostMarquee
import com.dcns.dailycost.foundation.theme.DailyCostTheme
import com.github.marlonlom.utilities.timeago.TimeAgo

@Preview
@Composable
private fun NotificationItemPreview() {
	DailyCostTheme {
		Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
			NotificationItem(
				notification = LocalNotificationDataProvider.notification1,
				onClick = {},
				modifier = Modifier
					.fillMaxWidth()
			)

			NotificationItem(
				notification = LocalNotificationDataProvider.notification2,
				onClick = {},
				modifier = Modifier
					.fillMaxWidth()
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationItem(
	notification: Notification,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {

	ElevatedCard(
		onClick = onClick,
		modifier = modifier,
		colors = CardDefaults.cardColors(
			containerColor = if (notification.hasBeenRead) DailyCostTheme.colorScheme.silverChalice
			else DailyCostTheme.colorScheme.wildSand
		),
		elevation = CardDefaults.elevatedCardElevation(
			defaultElevation = 2.dp
		)
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier
				.padding(8.dp)
		) {
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				Text(
					text = notification.title,
					style = MaterialTheme.typography.titleSmall.copy(
						fontWeight = FontWeight.SemiBold
					),
					modifier = Modifier
						.weight(1f)
						.dailyCostMarquee()
				)

				Text(
					text = TimeAgo.using(notification.sentTimeMillis),
					style = MaterialTheme.typography.bodySmall
				)
			}

			Text(
				text = notification.body,
				style = MaterialTheme.typography.bodySmall
			)
		}
	}
}
