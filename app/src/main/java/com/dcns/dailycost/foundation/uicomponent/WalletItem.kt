package com.dcns.dailycost.foundation.uicomponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dcns.dailycost.data.WalletType
import com.dcns.dailycost.data.model.Wallet
import com.dcns.dailycost.foundation.theme.DailyCostTheme

@Preview
@Composable
private fun WalletItemPreview() {
	DailyCostTheme {
		WalletItem(
			wallet = Wallet(WalletType.EWallet, 0.0),
			onClick = {},
			modifier = Modifier
				.fillMaxWidth()
		)
	}
}

@Preview
@Composable
private fun SelectableWalletItemPreview() {
	DailyCostTheme {
		SelectableWalletItem(
			selected = true,
			wallet = Wallet(WalletType.EWallet, 0.0),
			onClick = {},
			modifier = Modifier
				.fillMaxWidth()
		)
	}
}

@Composable
fun SelectableWalletItem(
	wallet: Wallet,
	selected: Boolean,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	WalletItem(
		wallet = wallet,
		modifier = modifier,
		onClick = onClick,
		trailing = {
			RadioButton(
				selected = selected,
				onClick = onClick,
				colors = RadioButtonDefaults.colors(
					selectedColor = DailyCostTheme.colorScheme.primary
				)
			)
		}
	)
}

@Composable
fun WalletItem(
	wallet: Wallet,
	modifier: Modifier = Modifier,
	trailing: @Composable (() -> Unit)? = null,
	onClick: () -> Unit
) {

	Card(
		colors = CardDefaults.cardColors(
			containerColor = Color.Transparent
		),
		modifier = Modifier
			.clickable { onClick() }
			.then(modifier)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.size(48.dp)
			) {
				Icon(
					painter = painterResource(wallet.walletType.icon),
					contentDescription = null
				)
			}

			Text(
				text = wallet.walletType.localizedName,
				modifier = Modifier
					.weight(1f)
			)

			Spacer(modifier = Modifier.width(8.dp))

			trailing?.invoke()
		}
	}
}
