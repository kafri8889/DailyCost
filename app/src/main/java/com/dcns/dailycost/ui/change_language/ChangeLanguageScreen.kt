package com.dcns.dailycost.ui.change_language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.foundation.uicomponent.DragHandle
import com.dcns.dailycost.foundation.uicomponent.LanguageItem

@Composable
fun ChangeLanguageScreen(
	viewModel: ChangeLanguageViewModel,
	navigationActions: NavigationActions
) {

	val state by viewModel.state.collectAsStateWithLifecycle()

	LazyColumn(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier
			.fillMaxWidth()
			.navigationBarsPadding()
	) {
		item {
			DragHandle(
				modifier = Modifier
					.padding(
						top = 16.dp,
						bottom = 8.dp
					)
			)
		}

		items(Language.values()) { language ->
			LanguageItem(
				language = language,
				selected = state.selectedLanguage == language,
				onClick = {
					viewModel.onAction(ChangeLanguageAction.ChangeLanguage(language))
				},
				modifier = Modifier
					.fillMaxWidth(0.96f)
			)
		}

		item {
			Spacer(modifier = Modifier.height(8.dp))
		}
	}
}
