package com.dcns.dailycost.ui.setting

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.common.DailyCostBiometricManager
import com.dcns.dailycost.foundation.uicomponent.BasicPreference
import com.dcns.dailycost.foundation.uicomponent.SwitchPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
	viewModel: SettingViewModel,
	navigationActions: NavigationActions,
	onNavigationIconClicked: () -> Unit
) {

	val context = LocalContext.current
	val uriHandler = LocalUriHandler.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	val canAuth = remember(context) {
		DailyCostBiometricManager.canAuthenticateWithAuthenticators(context)
	}

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(stringResource(id = R.string.advance_setting))
				},
				navigationIcon = {
					IconButton(onClick = onNavigationIconClicked) {
						Icon(
							painter = painterResource(id = R.drawable.ic_menu),
							contentDescription = null
						)
					}
				}
			)
		}
	) { scaffoldPadding ->
		LazyColumn(
			modifier = Modifier
				.padding(scaffoldPadding)
		) {
			item {
				SwitchPreference(
					enabled = canAuth,
					isChecked = state.isSecureAppEnabled,
					onCheckedChange = { checked ->
						viewModel.onAction(SettingAction.UpdateIsSecureAppEnabled(checked))
					},
					title = {
						Text(stringResource(id = R.string.secure_app))
					},
					summary = {
						Text(stringResource(id = R.string.secure_app_with_fingerprint))
					},
					icon = {
						Icon(
							painter = painterResource(id = R.drawable.ic_finger_scan),
							contentDescription = null
						)
					}
				)
			}

			item {
				BasicPreference(
					title = {
						Text(stringResource(id = R.string.terms_of_use))
					},
					onClick = {
						uriHandler.openUri(Constant.TERMS_OF_USE_URL)
					}
				)
			}

			item {
				BasicPreference(
					title = {
						Text(stringResource(id = R.string.privacy_policy))
					},
					onClick = {
						uriHandler.openUri(Constant.PRIVACY_POLICY_URL)
					}
				)
			}
		}
	}
}
