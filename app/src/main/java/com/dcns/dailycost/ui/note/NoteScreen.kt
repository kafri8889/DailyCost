package com.dcns.dailycost.ui.note

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dcns.dailycost.R
import com.dcns.dailycost.data.NavigationActions
import com.dcns.dailycost.data.TopLevelDestinations
import com.dcns.dailycost.foundation.base.BaseScreenWrapper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
	viewModel: NoteViewModel,
	navigationActions: NavigationActions
) {

	val context = LocalContext.current

	val state by viewModel.state.collectAsStateWithLifecycle()

	BackHandler {
		navigationActions.navigateTo(
			destination = TopLevelDestinations.Home.dashboard
		)
	}

	BaseScreenWrapper(
		viewModel = viewModel,
		topBar = {
			CenterAlignedTopAppBar(
				title = {
					Text(stringResource(id = R.string.add_note))
				},
				navigationIcon = {
					IconButton(
						onClick = {
							navigationActions.navigateTo(
								destination = TopLevelDestinations.Home.dashboard
							)
						}
					) {
						Icon(
							painter = painterResource(id = R.drawable.ic_arrow_left),
							contentDescription = null
						)
					}
				}
			)
		}

	) { scaffoldPadding ->
		CreateEditNoteForm(
			modifier = Modifier
				.padding(scaffoldPadding),
			viewModel = viewModel,
			state = state,
			onTitleChanged = { s ->
				viewModel.onAction(NoteAction.UpdateTitle(s))
			},
			onDescriptionChanged = { s ->
				viewModel.onAction(NoteAction.UpdateDescription(s))
			}
		)
	}
}


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CreateEditNoteForm(
	state: NoteState,
	modifier: Modifier = Modifier,
	onTitleChanged: (String) -> Unit = {},
	onDescriptionChanged: (String) -> Unit = {},
	viewModel: NoteViewModel,
) {
	val (
		titleFocusRequester,
		descriptionFocusRequester,
	) = FocusRequester.createRefs()

	val constraintSet = ConstraintSet {
		val (
			topContent,
			centerContent,
			bottomContent
		) = createRefsFor(
			"topContent",
			"centerContent",
			"bottomContent"
		)

		constrain(topContent) {
			centerHorizontallyTo(parent)

			top.linkTo(parent.top)
			bottom.linkTo(centerContent.top)
		}

		constrain(centerContent) {
			centerTo(parent)

			top.linkTo(topContent.bottom)
			bottom.linkTo(bottomContent.top)
		}

		constrain(bottomContent) {
			centerHorizontallyTo(parent)

			top.linkTo(centerContent.bottom)
			bottom.linkTo(parent.bottom)
		}
	}

	val openDialog = remember { mutableStateOf(false) }
	val date = state.date?.let { Date(it) }
	val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
	val formattedDate = format.format(date)
	val context = LocalContext.current

	ConstraintLayout(
		constraintSet = constraintSet,
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = modifier.padding(12.dp)
		) {
			OutlinedTextField(
				value = state.title,
				label = {
					Text(stringResource(id = R.string.notes_title))
				},
				singleLine = true,
				onValueChange = onTitleChanged,
				keyboardOptions = KeyboardOptions(
					imeAction = ImeAction.Next,
					keyboardType = KeyboardType.Text
				),
				modifier = Modifier
					.fillMaxWidth()
					.focusRequester(titleFocusRequester)
			)
			OutlinedTextField(
				value = state.description,
				label = {
					Text(stringResource(id = R.string.notes_body))
				},
				singleLine = true,
				onValueChange = onDescriptionChanged,
				keyboardOptions = KeyboardOptions(
					imeAction = ImeAction.Next,
					keyboardType = KeyboardType.Text
				),
				modifier = Modifier
					.fillMaxWidth()
					.focusRequester(descriptionFocusRequester)
			)
			OutlinedTextField(
				value = formattedDate,
				label = {
					Text(stringResource(id = R.string.date))
				},
				readOnly = true,
				singleLine = true,
				trailingIcon = {
					IconButton(
						onClick = {
							openDialog.value = true
						}
					) {
						Icon(
							painter = painterResource(R.drawable.ic_calendar),
							contentDescription = null
						)
					}
				},
				onValueChange = onDescriptionChanged,
				keyboardOptions = KeyboardOptions(
					imeAction = ImeAction.Next,
					keyboardType = KeyboardType.Text
				),
				modifier = Modifier
					.fillMaxWidth()
					.focusRequester(descriptionFocusRequester)
			)
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				// Decoupled snackbar host state from scaffold state for demo purposes.
				val snackState = remember { SnackbarHostState() }
				val snackScope = rememberCoroutineScope()
				SnackbarHost(hostState = snackState, Modifier)

				if (openDialog.value) {
					val datePickerState = rememberDatePickerState()
					val confirmEnabled =
						derivedStateOf { datePickerState.selectedDateMillis != null }
					DatePickerDialog(
						onDismissRequest = {
							// Dismiss the dialog when the user clicks outside the dialog or on the back
							// button. If you want to disable that functionality, simply use an empty
							// onDismissRequest.
							openDialog.value = false
						},
						confirmButton = {
							TextButton(
								onClick = {
									openDialog.value = false
									snackScope.launch {
										viewModel.onAction(NoteAction.UpdateDate(datePickerState.selectedDateMillis))
										snackState.showSnackbar(
											"Selected date timestamp: ${datePickerState.selectedDateMillis}"
										)
									}
								},
								enabled = confirmEnabled.value
							) {
								Text("OK")
							}
						},
						dismissButton = {
							TextButton(
								onClick = {
									openDialog.value = false
								}
							) {
								Text("Cancel")
							}
						}
					) {
						DatePicker(state = datePickerState)
					}
				}
			}
			Button(
				onClick = {
					state.toString()
					viewModel.onAction(NoteAction.CreateNote(context))
				},
				modifier = Modifier
					.fillMaxWidth()
			) {
				Text(
					stringResource(
						R.string.submit
					)
				)
			}
			RequestContentPermission(
				viewModel = viewModel
			)
		}
	}

}

@Composable
fun RequestContentPermission(
	viewModel: NoteViewModel
) {
	var imageUri by remember {
		mutableStateOf<Uri?>(null)
	}
	val context = LocalContext.current
	val bitmap = remember {
		mutableStateOf<Bitmap?>(null)
	}

	val launcher = rememberLauncherForActivityResult(
		contract =
		ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		imageUri = uri
		viewModel.onAction(NoteAction.UpdateImage(uri = uri))
	}
	Column() {
		Button(onClick = {
			launcher.launch("image/*")
		}) {
			Text(text = "Pick image")
		}

		Spacer(modifier = Modifier.height(12.dp))

		imageUri?.let {
			if (Build.VERSION.SDK_INT < 28) {
				bitmap.value = MediaStore.Images
					.Media.getBitmap(context.contentResolver, it)

			} else {
				val source = ImageDecoder
					.createSource(context.contentResolver, it)
				bitmap.value = ImageDecoder.decodeBitmap(source)
			}

			bitmap.value?.let { btm ->
				Image(
					bitmap = btm.asImageBitmap(),
					contentDescription = null,
					modifier = Modifier.size(400.dp)
				)
			}
		}

	}
}


@Preview("Create or Edit Notes Form", showBackground = true)
@Composable
fun LoginScreenContentPreview() {

	CreateEditNoteForm(
		state = NoteState(),
		viewModel = hiltViewModel()
	)
}
