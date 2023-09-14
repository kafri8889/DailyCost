package com.dcns.dailycost.foundation.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.dcns.dailycost.foundation.base.UiEvent.DismissCurrentSnackbar.parse
import com.dcns.dailycost.foundation.extension.toast
import com.dcns.dailycost.foundation.uicomponent.bubble_bar.BubbleBar
import com.dcns.dailycost.foundation.uicomponent.bubble_bar.BubbleBarHost
import com.dcns.dailycost.foundation.uicomponent.bubble_bar.BubbleBarHostState
import com.dcns.dailycost.foundation.uicomponent.bubble_bar.BubbleBarResult
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
fun <STATE: Any, ACTION> BaseScreenWrapper(
	viewModel: BaseViewModel<STATE, ACTION>,
	modifier: Modifier = Modifier,
	onEvent: (UiEvent) -> Unit = {},
	topBar: @Composable () -> Unit = {},
	bottomBar: @Composable () -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	content: @Composable (scaffoldPadding: PaddingValues) -> Unit
) {

	val context = LocalContext.current

	val bubbleBarHostState = remember { BubbleBarHostState() }
	val hostState = remember { SnackbarHostState() }

	LaunchedEffect(Unit) {
		viewModel.uiEvent.filterNotNull().collectLatest { event ->
			withContext(Dispatchers.Main) { onEvent(event) }
			when (event) {
				is UiEvent.DismissCurrentSnackbar -> hostState.currentSnackbarData?.dismiss()
				is UiEvent.ShowBubbleBar -> {
					val result = bubbleBarHostState.showBubble(
						message = event.message.parse(context),
						actionLabel = event.actionLabel?.parse(context),
						withDismissAction = event.withDismissAction,
						duration = event.duration
					)

					viewModel.sendEventResult(
						when (result) {
							BubbleBarResult.Dismissed -> UiEventResult.Dismissed(event)
							BubbleBarResult.ActionPerformed -> UiEventResult.ActionPerformed(event)
						}
					)
				}
				is UiEvent.ShowSnackbar -> {
					val result = hostState.showSnackbar(
						message = event.message.parse(context),
						actionLabel = event.actionLabel?.parse(context),
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
					event.message.parse(context).toast(context, event.length)
				}
			}
		}
	}

	BubbleBarHost(
		hostState = bubbleBarHostState,
		bubbleBar = { data ->
			BubbleBar(
				bubbleBarData = data,
				modifier = Modifier
					.statusBarsPadding()
			)
		}
	) {
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

}
