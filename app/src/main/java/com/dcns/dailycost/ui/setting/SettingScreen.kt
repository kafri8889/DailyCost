package com.dcns.dailycost.ui.setting

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.Constant
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import com.dcns.dailycost.foundation.extension.toast
import com.dcns.dailycost.foundation.uicomponent.BasicPreference

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

    val scope = rememberCoroutineScope()

    BackHandler {
        navigationActions.popBackStack(TopLevelDestinations.Home.dashboard.route)
    }

    BaseScreenWrapper(
        viewModel = viewModel,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(id = R.string.setting))
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
                BasicPreference(
                    title = {
                        Text(stringResource(id = R.string.language))
                    },
                    summary = { _, _ ->
                        Text(state.language.name)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_global),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        navigationActions.navigateTo(TopLevelDestinations.Home.changeLanguage)
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

            item {
                Divider()
            }

            item {
                BasicPreference(
                    title = {
                        Text(stringResource(id = R.string.logout))
                    },
                    summary = { maxLines, overflow ->
                        Text(
                            text = state.userCredential.email,
                            maxLines = maxLines,
                            overflow = overflow
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        context.getString(R.string.logout_success).toast(context)
                        navigationActions.navigateTo(TopLevelDestinations.LoginRegister.loginRegister)
                    }
                )
            }
        }
    }
}
