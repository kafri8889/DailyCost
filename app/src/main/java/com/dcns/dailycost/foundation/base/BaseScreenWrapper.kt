package com.dcns.dailycost.foundation.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.dcns.dailycost.foundation.extension.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext

/**
 * Kerangka dasar untuk screen
 *
 * @author kafri8889
 */
@Composable
fun <STATE, ACTION> BaseScreenWrapper(
	viewModel: BaseViewModel<STATE, ACTION>,
	modifier: Modifier = Modifier,
	onEvent: (UiEvent) -> Unit = {},
	topBar: @Composable () -> Unit = {},
	bottomBar: @Composable () -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	content: @Composable (scaffoldPadding: PaddingValues) -> Unit
) {

	val context = LocalContext.current

	val hostState = remember {
		SnackbarHostState()
	}

	LaunchedEffect(Unit) {
		viewModel.uiEvent.filterNotNull().collectLatest { event ->
			withContext(Dispatchers.Main) { onEvent(event) }
			when (event) {
				is UiEvent.DismissCurrentSnackbar -> hostState.currentSnackbarData?.dismiss()
				is UiEvent.ShowSnackbar -> {
					val result = hostState.showSnackbar(
						message = event.getMessage(context),
						actionLabel = event.getActionLabel(context),
						withDismissAction = event.withDismissAction,
						duration = event.duration
					)

					viewModel.sendEventResult(
						when (result) {
							Dismissed -> UiEventResult.Dismissed(event)
							ActionPerformed -> UiEventResult.ActionPerformed(event)
						}
					)
				}

				is UiEvent.ShowToast -> {
					event.getMessage(context).toast(context, event.length)
				}
			}
		}
	}

	Scaffold(
		topBar = topBar,
		bottomBar = bottomBar,
		floatingActionButton = floatingActionButton,
		modifier = modifier,
		snackbarHost = {
			SnackbarHost(hostState = hostState)
		}
	) { scaffoldPadding ->
		content(scaffoldPadding)
	}

}
