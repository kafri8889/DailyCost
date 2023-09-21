package com.dcns.dailycost.ui.wallets

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.WalletsScreenMode
import com.dcns.dailycost.data.model.Wallet
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.uicomponent.SelectableWalletItem
import com.dcns.dailycost.foundation.uicomponent.WalletItem
import com.dcns.dailycost.navigation.home.shared.HomeSharedAction
import com.dcns.dailycost.navigation.home.shared.HomeSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletsScreen(
	viewModel: WalletsViewModel,
	homeSharedViewModel: HomeSharedViewModel,
	navigationActions: NavigationActions
) {

	val state by viewModel.state.collectAsStateWithLifecycle()
	val sharedState by homeSharedViewModel.state.collectAsStateWithLifecycle()

	if (sharedState.selectedWalletType != null) {
		LaunchedEffect(sharedState.selectedWalletType) {
			viewModel.onAction(WalletsAction.ChangeSelectedWalletType(sharedState.selectedWalletType!!))
		}
	}

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			TopAppBar(
				title = {
					Text(
						stringResource(
							id = when (state.screenMode) {
								WalletsScreenMode.WalletList -> R.string.wallets
								WalletsScreenMode.SelectWallet -> R.string.select_wallet
							}
						)
					)
				},
				navigationIcon = {
					IconButton(onClick = navigationActions::popBackStack) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				},
				actions = {
					if (state.screenMode == WalletsScreenMode.SelectWallet) {
						IconButton(
							onClick = {
								homeSharedViewModel.onAction(HomeSharedAction.UpdateSelectedWalletType(state.selectedWalletType))
								navigationActions.popBackStack()
							}
						) {
							Icon(
								painter = painterResource(id = R.drawable.ic_check),
								contentDescription = stringResource(id = R.string.accessibility_save)
							)
						}
					}
				}
			)
		}
	) { scaffoldPadding ->
		WalletsScreenContent(
			state = state,
			onWalletClicked = { wallet ->
				if (state.screenMode == WalletsScreenMode.SelectWallet) {
					viewModel.onAction(WalletsAction.ChangeSelectedWalletType(wallet.walletType))
					return@WalletsScreenContent
				}
			},
			modifier = Modifier
				.fillMaxSize()
				.padding(scaffoldPadding)
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WalletsScreenContent(
	state: WalletsState,
	modifier: Modifier = Modifier,
	onWalletClicked: (Wallet) -> Unit
) {

	LazyColumn(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		items(
			items = state.wallets,
			key = { item -> item.walletType }
		) { wallet ->
			if (state.screenMode == WalletsScreenMode.WalletList) {
				WalletItem(
					wallet = wallet,
					onClick = {
						onWalletClicked(wallet)
					},
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(tween(256))
				)
			} else {
				SelectableWalletItem(
					wallet = wallet,
					selected = state.selectedWalletType == wallet.walletType,
					onClick = {
						onWalletClicked(wallet)
					},
					modifier = Modifier
						.fillMaxWidth()
						.animateItemPlacement(tween(256))
				)
			}
		}
	}
}
